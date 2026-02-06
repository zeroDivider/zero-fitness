package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "workout_sessions",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("workoutPlanId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val workoutPlanId: UUID?,
    val startedAtEpochMs: Long = System.currentTimeMillis(),
    val endedAtEpochMs: Long? = null,
    val notes: String? = null
)
