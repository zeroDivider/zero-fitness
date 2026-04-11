package com.zero.fitness.ui.screens.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zero.fitness.ui.screens.history.HistoryDestination
import com.zero.fitness.ui.screens.history.historyScreen
import com.zero.fitness.ui.screens.history.navigateToHistory
import com.zero.fitness.ui.screens.home.HomeDestination
import com.zero.fitness.ui.screens.home.homeScreen
import com.zero.fitness.ui.screens.home.navigateToHome
import com.zero.fitness.ui.screens.library.LibraryDestination
import com.zero.fitness.ui.screens.library.libraryScreen
import com.zero.fitness.ui.screens.library.navigateToLibrary
import com.zero.fitness.ui.screens.profile.ProfileDestination
import com.zero.fitness.ui.screens.profile.navigateToProfile
import com.zero.fitness.ui.screens.profile.profileScreen


@Composable
fun MainScreen(
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToWorkoutPreview: () -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { Spacer(modifier = Modifier.size(0.dp)) },
        bottomBar = {
            BottomBar(
                hierarchy = navController.currentBackStackEntryAsState().value?.destination?.hierarchy,
                onNavigateToHome = { navController.navigateToHome() },
                onNavigateToLibrary = { navController.navigateToLibrary() },
                onNavigateToHistory = { navController.navigateToHistory() },
                onNavigateToProfile = { navController.navigateToProfile() },
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = HomeDestination
        ) {
            homeScreen(
                onNavigateToCreateWorkout = onNavigateToCreateWorkout,
                onNavigateToWorkoutPreview = onNavigateToWorkoutPreview,
                onNavigateToWorkout = onNavigateToWorkout,
            )
            libraryScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToNewExercise = onNavigateToCreateExercise,
            )
            historyScreen(
                onNavigateUp = { navController.navigateUp() },
            )
            profileScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}

@Composable
private fun BottomBar(
    hierarchy: Sequence<NavDestination>?,
    onNavigateToHome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(HomeDestination::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Home, "home") },
            onClick = onNavigateToHome,
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(LibraryDestination::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Book, "library") },
            onClick = onNavigateToLibrary,
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(HistoryDestination::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Checklist, "history") },
            onClick = onNavigateToHistory,
        )
        NavigationBarItem(
            selected = hierarchy?.any { it.hasRoute(ProfileDestination::class) } == true,
            icon = { Icon(imageVector = Icons.Default.Person, "home") },
            onClick = onNavigateToProfile,
        )
    }
}
