package com.zero.fitness

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.zero.fitness.ui.screens.login.loginScreen
import com.zero.fitness.ui.screens.main.MainDestination
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
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween()
            )
        },
        exitTransition = {
            scaleOut(targetScale = .9f, animationSpec = tween())
        },
        popEnterTransition = {
            scaleIn(initialScale = .9f, animationSpec = tween())
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween()
            )
        },
        startDestination = MainDestination,
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
