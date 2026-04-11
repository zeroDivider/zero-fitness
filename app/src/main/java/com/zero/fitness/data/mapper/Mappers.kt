package com.zero.fitness.data.mapper

import com.zero.fitness.data.local.db.entity.*
import com.zero.fitness.data.remote.dto.ExerciseDto
import com.zero.fitness.domain.model.*
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import com.zero.fitness.data.local.db.entity.PlannedSetEntity
import com.zero.fitness.data.local.db.entity.SessionSetResultEntity
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.model.PlannedSet
import com.zero.fitness.domain.model.SessionSetResult
import com.zero.fitness.domain.model.Weight
import com.zero.fitness.domain.model.WorkoutTarget
import java.util.UUID


private fun String?.toStringList(): List<String> =
    this?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()

private fun List<String>.toCsv(): String? =
    this.map { it.trim() }.filter { it.isNotEmpty() }.takeIf { it.isNotEmpty() }?.joinToString(",")

fun ExerciseEntity.toDomain(): Exercise =
    Exercise(
        id = id,
        name = name,
        description = description,
        allowWeight = allowWeight,
        equipment = equipment?.let { Equipment.valueOf(it) },
        muscleGroups = muscleGroupsCsv.toStringList().map { MuscleGroup.valueOf(it) },
        difficulty = difficulty?.let { DifficultyLevel.valueOf(it) },
        isCompound = isCompound,
        videoUrl = videoUrl
    )

fun Exercise.toEntity(nowEpochMs: Long = System.currentTimeMillis()): ExerciseEntity =
    ExerciseEntity(
        id = id,
        name = name,
        description = description,
        allowWeight = allowWeight,
        equipment = equipment.toString(),
        muscleGroupsCsv = muscleGroups.map { it.name }.toCsv(),
        difficulty = difficulty?.name,
        isCompound = isCompound,
        videoUrl = videoUrl,
        updatedAtEpochMs = nowEpochMs,
    )

fun ExerciseEntity.toDto(): ExerciseDto =
    ExerciseDto(
        id = id.toString(),
        name = name,
        description = description,
        allowWeight = allowWeight,
        equipment = equipment,
        muscleGroupsCsv = muscleGroupsCsv,
        difficulty = difficulty,
        isCompound = isCompound,
        videoUrl = videoUrl,
        createdAtEpochMs = createdAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
    )

fun ExerciseDto.toEntity(
    syncPending: Boolean = false,
    deletedLocally: Boolean = false,
): ExerciseEntity =
    ExerciseEntity(
        id = UUID.fromString(id),
        name = name,
        description = description,
        allowWeight = allowWeight,
        equipment = equipment,
        muscleGroupsCsv = muscleGroupsCsv,
        difficulty = difficulty,
        isCompound = isCompound,
        videoUrl = videoUrl,
        createdAtEpochMs = createdAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
        syncPending = syncPending,
        deletedLocally = deletedLocally,
    )

fun PlannedSetEntity.toDomain(): PlannedSet {
    val target: com.zero.fitness.domain.model.WorkoutTarget = when {
        targetReps != null -> com.zero.fitness.domain.model.WorkoutTarget.Reps(targetReps)
        targetTimeSeconds != null -> com.zero.fitness.domain.model.WorkoutTarget.Time(targetTimeSeconds)
        targetDistanceMeters != null -> com.zero.fitness.domain.model.WorkoutTarget.Distance(targetDistanceMeters)
        else -> error("Invalid PlannedSetEntity($id): missing target")
    }

    val weight = if (targetWeightValue != null && targetWeightUnit != null) {
        Weight(targetWeightValue, targetWeightUnit)
    } else null

    return PlannedSet(
        id = id,
        orderIndex = orderIndex,
        target = target,
        targetWeight = weight,
        restAfterSetSeconds = restAfterSetSeconds,
        rpeTarget = rpeTarget,
        rirTarget = rirTarget,
        setType = setType
    )
}

fun PlannedSet.toEntity(workoutExerciseId: UUID): PlannedSetEntity {
    val (reps, time, dist) = when (val t = target) {
        is WorkoutTarget.Reps -> Triple(t.reps, null, null)
        is WorkoutTarget.Time -> Triple(null, t.seconds, null)
        is WorkoutTarget.Distance -> Triple(null, null, t.meters)
    }

    return PlannedSetEntity(
        id = id,
        workoutExerciseId = workoutExerciseId,
        orderIndex = orderIndex,
        targetReps = reps,
        targetTimeSeconds = time,
        targetDistanceMeters = dist,
        targetWeightValue = targetWeight?.value,
        targetWeightUnit = targetWeight?.unit,
        restAfterSetSeconds = restAfterSetSeconds,
        rpeTarget = rpeTarget,
        rirTarget = rirTarget,
        setType = setType
    )
}

fun SessionSetResultEntity.toDomain(): SessionSetResult {
    val actualTarget: WorkoutTarget? = when {
        actualReps != null -> WorkoutTarget.Reps(actualReps)
        actualTimeSeconds != null -> WorkoutTarget.Time(actualTimeSeconds)
        actualDistanceMeters != null -> WorkoutTarget.Distance(actualDistanceMeters)
        else -> null
    }

    val actualWeight = if (actualWeightValue != null && actualWeightUnit != null) {
        Weight(actualWeightValue, actualWeightUnit)
    } else null

    return SessionSetResult(
        id = id,
        sessionId = sessionId,
        blockId = blockId,
        exerciseId = workoutExerciseId,
        plannedSetId = plannedSetId,
        roundIndex = roundIndex,
        setOrderIndex = setOrderIndex,
        actualTarget = actualTarget,
        actualWeight = actualWeight,
        actualRestSeconds = actualRestSeconds,
        effortLevel = effortLevel,
        completed = completed,
        timestampEpochMs = timestampEpochMs
    )
}

fun SessionSetResult.toEntity(): SessionSetResultEntity {
    val (reps, time, dist) = when (val t = actualTarget) {
        null -> Triple(null, null, null)
        is WorkoutTarget.Reps -> Triple(t.reps, null, null)
        is WorkoutTarget.Time -> Triple(null, t.seconds, null)
        is WorkoutTarget.Distance -> Triple(null, null, t.meters)
    }

    return SessionSetResultEntity(
        id = id,
        sessionId = sessionId,
        blockId = blockId,
        workoutExerciseId = exerciseId,
        plannedSetId = plannedSetId,
        roundIndex = roundIndex,
        setOrderIndex = setOrderIndex,
        actualReps = reps,
        actualTimeSeconds = time,
        actualDistanceMeters = dist,
        actualWeightValue = actualWeight?.value,
        actualWeightUnit = actualWeight?.unit,
        actualRestSeconds = actualRestSeconds,
        effortLevel = effortLevel,
        completed = completed,
        timestampEpochMs = timestampEpochMs
    )
}
