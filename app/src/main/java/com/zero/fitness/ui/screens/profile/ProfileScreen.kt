package com.zero.fitness.ui.screens.profile

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
fun ProfileScreen(
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "Profile screen",
                subtitle = "This is your happy place!",
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
            Text(text = "Hello, PROFILE!")
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ProfileScreen({})
}
