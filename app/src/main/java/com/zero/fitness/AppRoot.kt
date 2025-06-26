package com.zero.fitness

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zero.fitness.ui.screens.login.LoginDestination
import com.zero.fitness.ui.screens.login.loginScreen
import com.zero.fitness.ui.screens.main.mainScreen
import com.zero.fitness.ui.screens.main.navigateToMain
import com.zero.fitness.ui.screens.workout.exercise.navigateToNewExercise
import com.zero.fitness.ui.screens.workout.exercise.newExerciseScreen
import com.zero.fitness.ui.screens.workout.new.navigateToNewWorkout
import com.zero.fitness.ui.screens.workout.new.newWorkoutScreen
import com.zero.fitness.ui.screens.workout.preview.navigateToPreviewWorkout
import com.zero.fitness.ui.screens.workout.preview.previewWorkoutScreen
import com.zero.fitness.ui.screens.workout.session.navigateToWorkoutSession
import com.zero.fitness.ui.screens.workout.session.workoutSessionScreen


@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        loginScreen(
            onNavigateToMain = { navController.navigateToMain() },
        )
        mainScreen(
            onNavigateToCreateWorkout = { navController.navigateToNewWorkout() },
            onNavigateToWorkoutPreview = { navController.navigateToPreviewWorkout() },
            onNavigateToWorkout = { navController.navigateToWorkoutSession() },
        )
        newWorkoutScreen(
            onNavigateToNewExercise = { navController.navigateToNewExercise() },
            onNavigateUp = { navController.navigateUp() },
        )
        newExerciseScreen(
            onNavigateUp = { navController.navigateUp() },
        )
        workoutSessionScreen(
            onNavigateUp = { navController.navigateUp() },
        )
        previewWorkoutScreen(
            onNavigateUp = { navController.navigateUp() },
        )
    }
}
