package com.zero.fitness.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreateWorkout: () -> Unit,
    onNavigateToWorkoutPreview: () -> Unit,
    onNavigateToWorkout: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home screen") },
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateToCreateWorkout,
                ) {
                    Text(text = "Create new workout")
                }
                Button(
                    onClick = onNavigateToWorkoutPreview,
                ) {
                    Text(text = "Preview workout")
                }
                Button(
                    onClick = onNavigateToWorkout,
                ) {
                    Text(text = "Start workout session")
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    HomeScreen({}, {}, {})
}
