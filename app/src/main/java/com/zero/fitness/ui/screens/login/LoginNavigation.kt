package com.zero.fitness.ui.screens.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object LoginDestination

fun NavGraphBuilder.loginScreen(
    onNavigateToMain: () -> Unit,
) {
    composable<LoginDestination> {
        LoginScreen(
            onNavigateToMain = onNavigateToMain,
        )
    }
}
