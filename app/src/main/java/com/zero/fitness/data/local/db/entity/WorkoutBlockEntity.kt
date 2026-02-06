package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zero.fitness.domain.model.BlockType
import java.util.UUID


@Entity(
    tableName = "workout_blocks",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutPlanId")]
)
data class WorkoutBlockEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val workoutPlanId: UUID,
    val type: BlockType,
    val orderIndex: Int,
    val rounds: Int? = null,
    val restAfterBlockSeconds: Int? = null
)
