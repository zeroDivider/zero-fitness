package com.zero.fitness.ui.screens.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object MainDestination

fun NavGraphBuilder.mainScreen(
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToWorkoutPreview: () -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    composable<MainDestination> {
        MainScreen(
            onNavigateToCreateExercise = onNavigateToCreateExercise,
            onNavigateToCreateWorkout = onNavigateToCreateWorkout,
            onNavigateToWorkoutPreview = onNavigateToWorkoutPreview,
            onNavigateToWorkout = onNavigateToWorkout,
        )
    }
}

fun NavController.navigateToMain() {
    navigate(MainDestination) {
        popUpTo(0) {
            inclusive = true
        }
    }
}
