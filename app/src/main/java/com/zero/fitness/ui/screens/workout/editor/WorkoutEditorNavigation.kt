package com.zero.fitness.ui.screens.workout.editor

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object WorkoutEditorDestination

fun NavGraphBuilder.workoutEditorScreen(
    onNavigateToCreateExercise: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    composable<WorkoutEditorDestination> {
        WorkoutEditorScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToCreateExercise = onNavigateToCreateExercise,
        )
    }
}

fun NavController.navigateToWorkoutEditor() {
    navigate(WorkoutEditorDestination)
}
