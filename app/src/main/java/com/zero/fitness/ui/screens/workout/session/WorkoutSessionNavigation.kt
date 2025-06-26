package com.zero.fitness.ui.screens.workout.session

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object WorkoutSessionDestination

fun NavGraphBuilder.workoutSessionScreen(
    onNavigateUp: () -> Unit,
) {
    composable<WorkoutSessionDestination> {
        WorkoutSessionScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToWorkoutSession() {
    navigate(WorkoutSessionDestination)
}
