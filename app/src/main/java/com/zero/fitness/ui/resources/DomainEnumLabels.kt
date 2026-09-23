package com.zero.fitness.ui.resources

import androidx.annotation.StringRes
import com.zero.fitness.R
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType

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
