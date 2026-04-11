package com.zero.fitness.ui.screens.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults


@Composable
fun LibraryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToNewExercise: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "Library screen",
                subtitle = "Add exercises by your own pace...",
                onBackClick = { onNavigateUp() },
            )
        }
    ) { padding ->
        Surface (
            modifier = Modifier.padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) { }
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "+ Add New Exercise",
                    colors = AppButtonDefaults.buttonColors(),
                    onClickAction = { onNavigateToNewExercise() }
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    LibraryScreen({}, {})
}
