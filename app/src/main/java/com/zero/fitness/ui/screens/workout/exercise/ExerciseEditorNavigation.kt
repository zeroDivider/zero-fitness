package com.zero.fitness.ui.screens.workout.exercise

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseEditorDestination(val exerciseId: String? = null)

fun NavGraphBuilder.exerciseEditorScreen(
    onNavigateUp: () -> Unit,
) {
    composable<ExerciseEditorDestination> {
        ExerciseEditorScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToExerciseEditor(exerciseId: UUID? = null) {
    navigate(ExerciseEditorDestination(exerciseId = exerciseId?.toString()))
}
