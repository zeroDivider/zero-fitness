package com.zero.fitness.data.remote

import com.zero.fitness.data.remote.dto.ExerciseDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseExerciseRemoteDataSource @Inject constructor(
    private val supabase: SupabaseClient,
) : ExerciseRemoteDataSource {

    override suspend fun fetchAll(): List<ExerciseDto> =
        supabase.from("exercises").select().decodeList<ExerciseDto>()

    override suspend fun upsert(dto: ExerciseDto) {
        supabase.from("exercises").upsert(dto)
    }

    override suspend fun delete(id: String) {
        supabase.from("exercises").delete {
            filter {
                eq("id", id)
            }
        }
    }
}
