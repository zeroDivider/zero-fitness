package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zero.fitness.domain.model.SetType
import com.zero.fitness.domain.model.WeightUnit
import java.util.UUID


@Entity(
    tableName = "step_sets",
    foreignKeys = [
        ForeignKey(
            entity = BlockStepEntity::class,
            parentColumns = ["id"],
            childColumns = ["stepId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("stepId")]
)
data class StepSetEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val stepId: UUID,
    val orderIndex: Int,

    val targetReps: Int? = null,
    val targetTimeSeconds: Int? = null,
    val targetDistanceMeters: Int? = null,

    val targetWeightValue: Double? = null,
    val targetWeightUnit: WeightUnit? = null,

    val restAfterSetSeconds: Int? = null,
    val rpeTarget: Double? = null,
    val rirTarget: Int? = null,

    val setType: SetType = SetType.WORK
)
