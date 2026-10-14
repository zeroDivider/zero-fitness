package com.zero.fitness.ui.screens.library

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data object LibraryDestination

fun NavGraphBuilder.libraryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToExerciseDetails: (UUID) -> Unit,
) {
    composable<LibraryDestination> {
        LibraryScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToCreateExercise = onNavigateToCreateExercise,
            onNavigateToExerciseDetails = onNavigateToExerciseDetails,
        )
    }
}

fun NavController.navigateToLibrary() {
    navigate(LibraryDestination)
}
