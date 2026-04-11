package com.zero.fitness.ui.screens.library

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LibraryDestination

fun NavGraphBuilder.libraryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToNewExercise: () -> Unit,
) {
    composable<LibraryDestination> {
        LibraryScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToNewExercise = onNavigateToNewExercise,
        )
    }
}

fun NavController.navigateToLibrary() {
    navigate(LibraryDestination)
}
