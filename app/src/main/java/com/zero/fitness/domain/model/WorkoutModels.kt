package com.zero.fitness.domain.model

import java.util.UUID


data class Exercise(
    val id: UUID,
    val name: String,
    val description: String? = null,
    val allowWeight: Boolean = true,
    val equipment: String? = null,
    val muscleGroups: List<String> = emptyList()
)

data class WorkoutTemplate(
    val id: UUID,
    val name: String,
    val description: String? = null,
    val blocks: List<WorkoutBlock> = emptyList()
)

data class WorkoutBlock(
    val id: UUID,
    val type: BlockType,
    val orderIndex: Int,
    val rounds: Int? = null,
    val restAfterBlockSeconds: Int? = null,
    val steps: List<BlockStep> = emptyList()
)

data class BlockStep(
    val id: UUID,
    val orderIndex: Int,
    val exerciseId: UUID,
    val notes: String? = null,
    val restAfterStepSeconds: Int? = null,
    val sets: List<StepSet> = emptyList()
)

data class StepSet(
    val id: UUID,
    val orderIndex: Int,
    val target: WorkoutTarget,
    val targetWeight: Weight? = null,
    val restAfterSetSeconds: Int? = null,
    val rpeTarget: Double? = null,
    val rirTarget: Int? = null,
    val setType: SetType = SetType.WORK
)

data class WorkoutSession(
    val id: UUID,
    val workoutTemplateId: UUID?,
    val startedAtEpochMs: Long,
    val endedAtEpochMs: Long? = null,
    val notes: String? = null
)

data class SessionSetResult(
    val id: UUID,
    val sessionId: UUID,
    val blockId: UUID,
    val stepId: UUID,
    val roundIndex: Int? = null,
    val setOrderIndex: Int,
    val actualTarget: WorkoutTarget? = null,
    val actualWeight: Weight? = null,
    val actualRestSeconds: Int? = null,
    val rpeActual: Double? = null,
    val rirActual: Int? = null,
    val completed: Boolean = true,
    val timestampEpochMs: Long
)
