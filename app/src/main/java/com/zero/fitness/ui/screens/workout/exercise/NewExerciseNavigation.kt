package com.zero.fitness.ui.screens.workout.exercise

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object NewExerciseDestination

fun NavGraphBuilder.newExerciseScreen(
    onNavigateUp: () -> Unit,
) {
    composable<NewExerciseDestination> {
        NewExerciseScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToNewExercise() {
    navigate(NewExerciseDestination)
}
