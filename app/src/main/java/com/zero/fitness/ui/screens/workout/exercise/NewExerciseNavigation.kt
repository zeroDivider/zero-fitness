package com.zero.fitness.ui.screens.workout.exercise

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class NewExerciseDestination(val exerciseId: String = "")

fun NavGraphBuilder.newExerciseScreen(
    onNavigateUp: () -> Unit,
) {
    composable<NewExerciseDestination> {
        NewExerciseScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToNewExercise(exerciseId: String? = null) {
    navigate(NewExerciseDestination(exerciseId = exerciseId.orEmpty()))
}

fun NavController.navigateToNewExercise(exerciseId: UUID) {
    navigate(NewExerciseDestination(exerciseId = exerciseId.toString()))
}
