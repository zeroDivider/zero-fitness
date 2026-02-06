package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zero.fitness.domain.model.SetType
import com.zero.fitness.domain.model.WeightUnit
import java.util.UUID


@Entity(
    tableName = "planned_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutExerciseId")]
)
data class PlannedSetEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val workoutExerciseId: UUID,
    val orderIndex: Int,

    val targetReps: Int? = null,
    val targetTimeSeconds: Int? = null,
    val targetDistanceMeters: Int? = null,

    val targetWeightValue: Double? = null,
    val targetWeightUnit: WeightUnit? = null,

    val restAfterSetSeconds: Int? = null,
    val effortLevel: Double? = null,

    val setType: SetType = SetType.WORK
)
