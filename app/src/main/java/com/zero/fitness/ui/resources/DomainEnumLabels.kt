package com.zero.fitness.ui.resources

import androidx.annotation.StringRes
import com.zero.fitness.R
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup

@StringRes
fun Equipment.labelRes(): Int = when (this) {
    Equipment.BODYWEIGHT -> R.string.equipment_bodyweight
    Equipment.BARBELL -> R.string.equipment_barbell
    Equipment.DUMBBELL -> R.string.equipment_dumbbell
    Equipment.KETTLEBELL -> R.string.equipment_kettlebell
    Equipment.CABLE -> R.string.equipment_cable
    Equipment.MACHINE -> R.string.equipment_machine
    Equipment.RESISTANCE_BAND -> R.string.equipment_resistance_band
}

@StringRes
fun ExerciseType.labelRes(): Int = when (this) {
    ExerciseType.COMPOUND -> R.string.exercise_type_compound
    ExerciseType.ISOLATION -> R.string.exercise_type_isolation
}

@StringRes
fun MuscleGroup.labelRes(): Int = when (this) {
    MuscleGroup.CHEST -> R.string.muscle_group_chest
    MuscleGroup.SHOULDERS -> R.string.muscle_group_shoulders
    MuscleGroup.TRICEPS -> R.string.muscle_group_triceps
    MuscleGroup.LATS -> R.string.muscle_group_lats
    MuscleGroup.UPPER_BACK -> R.string.muscle_group_upper_back
    MuscleGroup.TRAPS -> R.string.muscle_group_traps
    MuscleGroup.BICEPS -> R.string.muscle_group_biceps
    MuscleGroup.FOREARMS -> R.string.muscle_group_forearms
    MuscleGroup.CORE -> R.string.muscle_group_core
    MuscleGroup.LOWER_BACK -> R.string.muscle_group_lower_back
    MuscleGroup.QUADS -> R.string.muscle_group_quads
    MuscleGroup.HAMSTRINGS -> R.string.muscle_group_hamstrings
    MuscleGroup.GLUTES -> R.string.muscle_group_glutes
    MuscleGroup.ADDUCTORS -> R.string.muscle_group_adductors
    MuscleGroup.ABDUCTORS -> R.string.muscle_group_abductors
    MuscleGroup.CALVES -> R.string.muscle_group_calves
}

@StringRes
fun DifficultyLevel.labelRes(): Int = when (this) {
    DifficultyLevel.BEGINNER -> R.string.difficulty_beginner
    DifficultyLevel.INTERMEDIATE -> R.string.difficulty_intermediate
    DifficultyLevel.ADVANCED -> R.string.difficulty_advanced
    DifficultyLevel.ELITE -> R.string.difficulty_elite
}
