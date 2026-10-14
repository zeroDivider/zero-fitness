package com.zero.fitness.ui.screens.workout.preview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object WorkoutPreviewDestination

fun NavGraphBuilder.workoutPreviewScreen(
    onNavigateUp: () -> Unit,
) {
    composable<WorkoutPreviewDestination> {
        WorkoutPreviewScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToWorkoutPreview() {
    navigate(WorkoutPreviewDestination)
}
