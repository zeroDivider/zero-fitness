package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val allowWeight: Boolean = true,
    val equipment: String? = null,
    val muscleGroupsCsv: String? = null, // "chest,triceps"
    val createdAtEpochMs: Long = System.currentTimeMillis(),
    val updatedAtEpochMs: Long = System.currentTimeMillis()
)
