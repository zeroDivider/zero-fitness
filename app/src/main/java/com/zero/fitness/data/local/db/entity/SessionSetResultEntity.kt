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
            entity = BlockStepEntity::class,
            parentColumns = ["id"],
            childColumns = ["stepId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("sessionId"), Index("blockId"), Index("stepId")]
)
data class SessionSetResultEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val sessionId: UUID,

    val blockId: UUID,
    val stepId: UUID,

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
