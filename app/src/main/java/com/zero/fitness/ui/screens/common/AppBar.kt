package com.zero.fitness.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.theme.Coral
import com.zero.fitness.ui.theme.Purple


object AppBarDefaults {
    @Immutable
    data class Colors(
        val background: Brush,
        val title: Color,
        val navIcon: Color,
    )

    @Composable
    fun colors(
        background: Brush = Brush.linearGradient(colors = listOf(Coral, Purple)),
        titleColor: Color = Color.White,
        navIconColor: Color = Color.White,
    ): Colors = Colors(background, titleColor, navIconColor)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    subtitle: String = "",
    onBackClick: () -> Unit,
    actionButton: @Composable () -> Unit = {},
    appBarColors: AppBarDefaults.Colors = AppBarDefaults.colors()
) {
    TopAppBar(
        modifier = Modifier
            .background(
                brush = appBarColors.background,
            )
            .padding(
                vertical = 20.dp,
                horizontal = 12.dp,
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .background(
                        color = appBarColors.navIcon.copy(alpha = 0.3f),
                    ),
                onClick = onBackClick,
            ) {
                Icon(
                    AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = appBarColors.navIcon,
                )
            }
        },
        title = {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    color = appBarColors.title,
                    fontWeight = FontWeight.Bold,
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        color = appBarColors.title,
                        text = subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        },
        actions = { actionButton() },
    )
}

@Composable
@Preview
private fun Preview() {
    AppBar(
        title = "Hello, world!",
        subtitle = "Nije tebi lako!",
        actionButton = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White,
                )
            }
        },
        onBackClick = { },
    )
}
