package com.zero.fitness.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE deletedLocally = 0")
    fun observeAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getById(id: UUID): ExerciseEntity?

    @Upsert
    suspend fun upsert(entity: ExerciseEntity)

    @Upsert
    suspend fun upsertAll(entities: List<ExerciseEntity>)

    @Query(
        """
        UPDATE exercises
        SET deletedLocally = 1, syncPending = 1, updatedAtEpochMs = :updatedAtEpochMs
        WHERE id = :id
        """,
    )
    suspend fun markDeleted(id: UUID, updatedAtEpochMs: Long)

    @Query("SELECT * FROM exercises WHERE syncPending = 1")
    suspend fun getPending(): List<ExerciseEntity>

    @Query("UPDATE exercises SET syncPending = 0 WHERE id = :id")
    suspend fun markSynced(id: UUID)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun hardDelete(id: UUID)
}
