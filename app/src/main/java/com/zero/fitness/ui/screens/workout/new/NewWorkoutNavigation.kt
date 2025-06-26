package com.zero.fitness.ui.screens.workout.new

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object NewWorkoutDestination

fun NavGraphBuilder.newWorkoutScreen(
    onNavigateToNewExercise: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    composable<NewWorkoutDestination> {
        NewWorkoutScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToNewExercise = onNavigateToNewExercise,
        )
    }
}

fun NavController.navigateToNewWorkout() {
    navigate(NewWorkoutDestination)
}
