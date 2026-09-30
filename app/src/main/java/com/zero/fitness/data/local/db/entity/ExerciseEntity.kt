package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val nameSr: String? = null,
    val descriptionSr: String? = null,
    val allowWeight: Boolean = true,
    val equipment: String? = null,
    val muscleGroupsCsv: String? = null, // "chest,triceps"
    val difficulty: String? = null, // DifficultyLevel enum as string
    val isCompound: Boolean = true,
    val videoUrl: String? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis(),
    val updatedAtEpochMs: Long = System.currentTimeMillis(),
    val syncPending: Boolean = true,
    val deletedLocally: Boolean = false,
)
