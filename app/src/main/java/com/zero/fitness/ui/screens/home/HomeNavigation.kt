package com.zero.fitness.ui.screens.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen(
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToWorkoutPreview: () -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    composable<HomeDestination> {
        HomeScreen(
            onNavigateToCreateWorkout = onNavigateToCreateWorkout,
            onNavigateToWorkoutPreview = onNavigateToWorkoutPreview,
            onNavigateToWorkout = onNavigateToWorkout,
        )
    }
}

fun NavController.navigateToHome() {
    navigate(HomeDestination)
}
