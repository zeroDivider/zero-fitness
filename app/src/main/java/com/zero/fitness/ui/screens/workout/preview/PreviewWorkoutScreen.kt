package com.zero.fitness.ui.screens.workout.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zero.fitness.ui.screens.common.AppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewWorkoutScreen(
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "Workout preview",
                onBackClick = onNavigateUp,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewWorkoutScreen {}
}
