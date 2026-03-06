package com.zero.fitness.ui.screens.workout.new

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.theme.DarkPurple
import com.zero.fitness.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewWorkoutScreen(
    onNavigateToNewExercise: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "New workout",
                subtitle = "Never give up, create!",
                onBackClick = onNavigateUp,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            LazyColumn {}
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "+ Add exercise",
                    colors = AppButtonDefaults.buttonColors(
                        background = Yellow,
                        label = DarkPurple,
                    ),
                    onClickAction = onNavigateToNewExercise,
                )
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Save workout",
                    onClickAction = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NewWorkoutScreen({}) {}
}
