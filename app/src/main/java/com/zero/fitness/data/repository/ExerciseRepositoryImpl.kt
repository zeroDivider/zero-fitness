package com.zero.fitness.data.repository

import com.zero.fitness.data.local.db.dao.ExerciseDao
import com.zero.fitness.data.mapper.toDomain
import com.zero.fitness.data.mapper.toDto
import com.zero.fitness.data.mapper.toEntity
import com.zero.fitness.data.remote.ExerciseRemoteDataSource
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.repository.ExerciseRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val dao: ExerciseDao,
    private val remote: ExerciseRemoteDataSource,
) : ExerciseRepository {

    override fun observeAll(): Flow<List<Exercise>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: UUID): Exercise? =
        dao.getById(id)?.toDomain()

    override suspend fun save(exercise: Exercise) {
        dao.upsert(exercise.toEntity().copy(syncPending = true))
    }

    override suspend fun delete(id: UUID) {
        dao.markDeleted(id, System.currentTimeMillis())
    }

    override suspend fun sync() {
        pushPending()
        pullRemote()
    }

    private suspend fun pushPending() {
        dao.getPending().forEach { entity ->
            runCatching {
                if (entity.deletedLocally) {
                    remote.delete(entity.id.toString())
                    dao.hardDelete(entity.id)
                } else {
                    remote.upsert(entity.toDto())
                    dao.markSynced(entity.id)
                }
            }
        }
    }

    private suspend fun pullRemote() {
        val remoteDtos = runCatching { remote.fetchAll() }.getOrNull() ?: return
        dao.upsertAll(remoteDtos.map { it.toEntity(syncPending = false) })
    }
}
