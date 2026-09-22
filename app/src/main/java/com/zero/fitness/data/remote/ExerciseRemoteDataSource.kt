package com.zero.fitness.data.remote

import com.zero.fitness.data.remote.dto.ExerciseDto

interface ExerciseRemoteDataSource {
    suspend fun fetchAll(): List<ExerciseDto>
    suspend fun upsert(dto: ExerciseDto)
    suspend fun delete(id: String)
}
