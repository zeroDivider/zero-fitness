package com.zero.fitness.ui.screens.workout.exercise

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDetailsDestination(val exerciseId: String)

fun NavGraphBuilder.exerciseDetailsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToUpdate: (UUID) -> Unit,
) {
    composable<ExerciseDetailsDestination> {
        ExerciseDetailsScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToUpdate = onNavigateToUpdate,
        )
    }
}

fun NavController.navigateToExerciseDetails(exerciseId: UUID) {
    navigate(ExerciseDetailsDestination(exerciseId.toString()))
}
