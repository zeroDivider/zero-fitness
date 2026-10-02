package com.zero.fitness.domain.repository

import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.SyncReport
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun observeAll(): Flow<List<Exercise>>
    fun observePendingCount(): Flow<Int>
    fun observeById(id: UUID): Flow<Exercise?>
    suspend fun getById(id: UUID): Result<Exercise?>
    suspend fun save(exercise: Exercise): Result<Unit>
    suspend fun delete(id: UUID): Result<Unit>
    suspend fun sync(): Result<SyncReport>
}
