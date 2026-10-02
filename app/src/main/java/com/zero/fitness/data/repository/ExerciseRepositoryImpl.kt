package com.zero.fitness.data.repository

import com.zero.fitness.core.result.AppError
import com.zero.fitness.core.result.safeCall
import com.zero.fitness.data.local.db.dao.ExerciseDao
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import com.zero.fitness.data.mapper.toDomain
import com.zero.fitness.data.mapper.toDto
import com.zero.fitness.data.mapper.toEntity
import com.zero.fitness.data.remote.ExerciseRemoteDataSource
import com.zero.fitness.data.remote.dto.ExerciseDto
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.SyncReport
import com.zero.fitness.domain.repository.ExerciseRepository
import com.zero.fitness.domain.sync.SyncPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDao,
    private val remote: ExerciseRemoteDataSource,
    private val syncPolicy: SyncPolicy,
) : ExerciseRepository {

    override fun observeAll(): Flow<List<Exercise>> =
        dao.observeAll().map { entities -> entities.mapNotNull { it.toDomain() } }

    override fun observePendingCount(): Flow<Int> = dao.observePendingCount()

    override suspend fun getById(id: UUID): Result<Exercise?> = safeCall {
        dao.getById(id)?.toDomain()
    }

    override suspend fun save(exercise: Exercise): Result<Unit> = safeCall {
        dao.upsert(
            exercise.copy(updatedAtEpochMs = now()).toEntity(syncPending = syncPolicy.canSync()),
        )
    }

    override suspend fun delete(id: UUID): Result<Unit> = safeCall {
        if (dao.getRaw(id) == null) return@safeCall
        if (!syncPolicy.canSync()) {
            dao.hardDelete(id)
        } else {
            dao.markDeleted(id, pending = true, now())
        }
    }

    override suspend fun sync(): Result<SyncReport> {
        if (!syncPolicy.canSync()) {
            return Result.success(SyncReport(0, 0, 0, skipped = true))
        }
        return safeCall {
            val (pushed, failed) = pushPending()
            val dtos = safeCall { remote.fetchAll() }.getOrElse { error ->
                return Result.failure(error)
            }
            SyncReport(pushed, failed, applyPull(dtos))
        }
    }

    private suspend fun pushPending(): Pair<Int, Int> {
        val pending = dao.getPending()
        var pushed = 0
        var failed = 0
        for (index in pending.indices) {
            val result = safeCall { pushOne(pending[index]) }
            if (result.isSuccess) {
                pushed += 1
                continue
            }
            val error = result.exceptionOrNull()
            if (error is AppError.Network || error is AppError.Auth) {
                failed += pending.size - index
                break
            }
            failed += 1
        }
        return pushed to failed
    }

    private suspend fun pushOne(entity: ExerciseEntity) {
        if (entity.deletedLocally) {
            remote.delete(entity.id.toString())
            dao.hardDelete(entity.id)
        } else {
            remote.upsert(entity.toDto())
            dao.markSynced(entity.id, entity.updatedAtEpochMs)
        }
    }

    private suspend fun applyPull(dtos: List<ExerciseDto>): Int {
        val pendingIds = dao.getPending().map { it.id }.toSet()
        val entities = dtos.mapNotNull { dto ->
            val entity = dto.toEntity() ?: return@mapNotNull null
            if (entity.id in pendingIds) null else entity
        }
        dao.upsertAll(entities)
        val remoteIds = dtos.map { it.id }.toSet()
        val staleIds = dao.getSyncedIds().filter { it.toString() !in remoteIds }
        if (staleIds.isNotEmpty()) {
            dao.deleteSyncedByIds(staleIds)
        }
        return entities.size
    }

    private fun now(): Long = System.currentTimeMillis()
}
