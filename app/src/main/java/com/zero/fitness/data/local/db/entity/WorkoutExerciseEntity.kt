package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "workout_exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutBlockEntity::class,
            parentColumns = ["id"],
            childColumns = ["blockId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("blockId"), Index("exerciseId")]
)
data class WorkoutExerciseEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val blockId: UUID,
    val orderIndex: Int,
    val exerciseId: UUID,
    val notes: String? = null,
    val restAfterExerciseSeconds: Int? = null
)
