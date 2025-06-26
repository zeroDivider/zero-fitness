package com.zero.fitness.ui.screens.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HistoryDestination

fun NavGraphBuilder.historyScreen(
    onNavigateUp: () -> Unit,
) {
    composable<HistoryDestination> {
        HistoryScreen(
            onNavigateUp = onNavigateUp,
        )
    }
}

fun NavController.navigateToHistory() {
    navigate(HistoryDestination) {
        popUpTo(graph.id)
    }
}
