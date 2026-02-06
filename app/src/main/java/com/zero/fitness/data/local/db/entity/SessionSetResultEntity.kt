package com.zero.fitness.data.local.db.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zero.fitness.domain.model.WeightUnit
import java.util.UUID

@Entity(
    tableName = "session_set_results",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WorkoutBlockEntity::class,
            parentColumns = ["id"],
            childColumns = ["blockId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PlannedSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["plannedSetId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("sessionId"), Index("blockId"), Index("workoutExerciseId"), Index("plannedSetId")]
)
data class SessionSetResultEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val sessionId: UUID,

    val blockId: UUID,
    val workoutExerciseId: UUID,
    val plannedSetId: UUID?,

    val roundIndex: Int? = null,
    val setOrderIndex: Int,

    val actualReps: Int? = null,
    val actualTimeSeconds: Int? = null,
    val actualDistanceMeters: Int? = null,

    val actualWeightValue: Double? = null,
    val actualWeightUnit: WeightUnit? = null,

    val actualRestSeconds: Int? = null,
    val rpeActual: Double? = null,
    val rirActual: Int? = null,

    val completed: Boolean = true,
    val timestampEpochMs: Long = System.currentTimeMillis()
)
