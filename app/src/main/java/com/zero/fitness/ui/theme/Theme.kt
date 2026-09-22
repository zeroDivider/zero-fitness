package com.zero.fitness.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    onPrimary = Black,
    primaryContainer = WhiteSmoke,
    secondary = DarkPurple,
    onSecondary = White,
    tertiary = Coral,
    onTertiary = White,
    surface = White,
    onSurface = TextPrimary,
    onSurfaceVariant = Gray,
    outline = BorderLight,
    secondaryContainer = Yellow,
    onSecondaryContainer = DarkPurple,
)

// todo // dark theme creation
private val LightColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = Black,
    primaryContainer = WhiteSmoke,
    secondary = DarkPurple,
    onSecondary = White,
    tertiary = Coral,
    onTertiary = White,
    surface = White,
    onSurface = TextPrimary,
    onSurfaceVariant = Gray,
    outline = BorderLight,
    secondaryContainer = Yellow,
    onSecondaryContainer = DarkPurple,
)

@Composable
fun ZeroFitnessTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
