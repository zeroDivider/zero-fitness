package com.zero.fitness.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises WHERE deletedLocally = 0 ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<ExerciseEntity>>

    @Query("SELECT COUNT(*) FROM exercises WHERE syncPending = 1")
    fun observePendingCount(): Flow<Int>

    @Query("SELECT * FROM exercises WHERE id = :id AND deletedLocally = 0")
    suspend fun getById(id: UUID): ExerciseEntity?

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getRaw(id: UUID): ExerciseEntity?

    @Upsert
    suspend fun upsert(entity: ExerciseEntity)

    @Upsert
    suspend fun upsertAll(entities: List<ExerciseEntity>)

    @Query(
        """
        UPDATE exercises
        SET deletedLocally = 1, syncPending = :pending, updatedAtEpochMs = :updatedAtEpochMs
        WHERE id = :id
        """,
    )
    suspend fun markDeleted(id: UUID, pending: Boolean, updatedAtEpochMs: Long)

    @Query("SELECT * FROM exercises WHERE syncPending = 1")
    suspend fun getPending(): List<ExerciseEntity>

    @Query(
        """
        UPDATE exercises
        SET syncPending = 0
        WHERE id = :id AND updatedAtEpochMs = :pushedUpdatedAtEpochMs
        """,
    )
    suspend fun markSynced(id: UUID, pushedUpdatedAtEpochMs: Long)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun hardDelete(id: UUID)

    @Query("SELECT id FROM exercises WHERE syncPending = 0")
    suspend fun getSyncedIds(): List<UUID>

    @Query("DELETE FROM exercises WHERE id IN (:ids) AND syncPending = 0")
    suspend fun deleteSyncedByIds(ids: List<UUID>)
}
