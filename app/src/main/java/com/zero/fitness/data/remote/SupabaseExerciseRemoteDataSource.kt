package com.zero.fitness.data.remote

import com.zero.fitness.data.remote.dto.ExerciseDto
import dagger.Lazy
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseExerciseRemoteDataSource @Inject constructor(
    private val supabaseProvider: Lazy<SupabaseClient>,
) : ExerciseRemoteDataSource {

    // First access builds the client; withContext keeps that work off the main thread.
    private suspend fun client(): SupabaseClient =
        withContext(Dispatchers.IO) { supabaseProvider.get() }

    override suspend fun fetchAll(): List<ExerciseDto> =
        client().from("exercises").select().decodeList<ExerciseDto>()

    override suspend fun upsert(dto: ExerciseDto) {
        client().from("exercises").upsert(dto)
    }

    override suspend fun delete(id: String) {
        client().from("exercises").delete {
            filter {
                eq("id", id)
            }
        }
    }
}
