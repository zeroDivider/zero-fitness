package com.zero.fitness.ui.screens.workout.preview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object PreviewWorkoutDestination

fun NavGraphBuilder.previewWorkoutScreen(
    onNavigateUp: () -> Unit,
) {
    composable<PreviewWorkoutDestination> {
        PreviewWorkoutScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToPreviewWorkout() {
    navigate(PreviewWorkoutDestination)
}
