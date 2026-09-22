package com.zero.fitness.domain.repository

import com.zero.fitness.domain.model.Exercise
import java.util.UUID
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun observeAll(): Flow<List<Exercise>>
    suspend fun getById(id: UUID): Exercise?
    suspend fun save(exercise: Exercise)
    suspend fun delete(id: UUID)
    suspend fun sync()
}
