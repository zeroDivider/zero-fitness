package com.zero.fitness.ui.screens.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.screens.common.AppBar


@Composable
fun HistoryScreen(
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "History screen",
                onBackClick = { onNavigateUp() },
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, bottom = 12.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = "Hello, histEry!")
        }
    }
}

@Composable
@Preview
private fun Preview() {
    HistoryScreen {}
}
