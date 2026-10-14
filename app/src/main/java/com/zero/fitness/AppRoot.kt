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
import com.zero.fitness.ui.screens.workout.editor.navigateToWorkoutEditor
import com.zero.fitness.ui.screens.workout.editor.workoutEditorScreen
import com.zero.fitness.ui.screens.workout.exercise.exerciseDetailsScreen
import com.zero.fitness.ui.screens.workout.exercise.exerciseEditorScreen
import com.zero.fitness.ui.screens.workout.exercise.navigateToExerciseDetails
import com.zero.fitness.ui.screens.workout.exercise.navigateToExerciseEditor
import com.zero.fitness.ui.screens.workout.preview.navigateToWorkoutPreview
import com.zero.fitness.ui.screens.workout.preview.workoutPreviewScreen
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
            onNavigateToCreateExercise = { navController.navigateToExerciseEditor() },
            onNavigateToExerciseDetails = { id -> navController.navigateToExerciseDetails(id) },
            onNavigateToCreateWorkout = { navController.navigateToWorkoutEditor() },
            onNavigateToWorkoutPreview = { navController.navigateToWorkoutPreview() },
            onNavigateToWorkout = { navController.navigateToWorkoutSession() },
        )
        workoutEditorScreen(
            onNavigateToCreateExercise = { navController.navigateToExerciseEditor() },
            onNavigateUp = { navController.navigateUp() },
        )
        exerciseEditorScreen(
            onNavigateUp = { navController.navigateUp() },
        )
        exerciseDetailsScreen(
            onNavigateUp = { navController.navigateUp() },
            onNavigateToEditExercise = { id -> navController.navigateToExerciseEditor(id) },
        )
        workoutSessionScreen(
            onNavigateUp = { navController.navigateUp() },
        )
        workoutPreviewScreen(
            onNavigateUp = { navController.navigateUp() },
        )
    }
}
