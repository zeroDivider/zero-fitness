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
            entity = WorkoutTemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutTemplateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutTemplateId")]
)
data class WorkoutBlockEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val workoutTemplateId: UUID,
    val type: BlockType,
    val orderIndex: Int,
    val rounds: Int? = null,
    val restAfterBlockSeconds: Int? = null
)
