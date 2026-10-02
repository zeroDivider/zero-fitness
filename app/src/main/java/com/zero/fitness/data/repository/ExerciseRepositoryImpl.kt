package com.zero.fitness.data.repository

import com.zero.fitness.core.result.safeCall
import com.zero.fitness.data.local.db.dao.ExerciseDao
import com.zero.fitness.data.mapper.toDomain
import com.zero.fitness.data.mapper.toDto
import com.zero.fitness.data.mapper.toEntity
import com.zero.fitness.data.remote.ExerciseRemoteDataSource
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.SyncReport
import com.zero.fitness.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDao,
    private val remote: ExerciseRemoteDataSource,
) : ExerciseRepository {

    override fun observeAll(): Flow<List<Exercise>> =
        dao.observeAll().map { entities -> entities.mapNotNull { it.toDomain() } }

    override fun observePendingCount(): Flow<Int> = dao.observePendingCount()

    override suspend fun getById(id: UUID): Result<Exercise?> = safeCall {
        dao.getById(id)?.toDomain()
    }

    override suspend fun save(exercise: Exercise): Result<Unit> = safeCall {
        dao.upsert(exercise.toEntity(syncPending = true))
    }

    override suspend fun delete(id: UUID): Result<Unit> = safeCall {
        dao.markDeleted(id, pending = true, System.currentTimeMillis())
    }

    override suspend fun sync(): Result<SyncReport> = safeCall {
        pushPending()
        pullRemote()
        SyncReport(pushed = 0, failed = 0, pulled = 0)
    }

    private suspend fun pushPending() {
        dao.getPending().forEach { entity ->
            runCatching {
                if (entity.deletedLocally) {
                    remote.delete(entity.id.toString())
                    dao.hardDelete(entity.id)
                } else {
                    remote.upsert(entity.toDto())
                    dao.markSynced(entity.id, entity.updatedAtEpochMs)
                }
            }
        }
    }

    private suspend fun pullRemote() {
        val remoteDtos = runCatching { remote.fetchAll() }.getOrNull() ?: return
        dao.upsertAll(remoteDtos.mapNotNull { it.toEntity() })
    }
}
