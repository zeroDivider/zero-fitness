package com.zero.fitness.ui.screens.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ScrollEdgeFadeDefaults {
    @Immutable
    data class Sizes(
        val fadeHeight: Dp,
    )

    @Immutable
    data class Colors(
        val topFadeBrush: Brush,
        val bottomFadeBrush: Brush,
    )

    @Composable
    fun sizes(fadeHeight: Dp = 20.dp): Sizes = Sizes(fadeHeight = fadeHeight)

    @Composable
    fun colors(fadeColor: Color = MaterialTheme.colorScheme.surface): Colors = Colors(
        topFadeBrush = Brush.verticalGradient(
            colors = listOf(fadeColor, Color.Transparent),
        ),
        bottomFadeBrush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, fadeColor),
        ),
    )
}

@Composable
private fun ScrollEdgeFadeTop(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    sizes: ScrollEdgeFadeDefaults.Sizes = ScrollEdgeFadeDefaults.sizes(),
    colors: ScrollEdgeFadeDefaults.Colors = ScrollEdgeFadeDefaults.colors(),
) {
    if (!visible) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(sizes.fadeHeight)
            .background(colors.topFadeBrush),
    )
}

@Composable
private fun ScrollEdgeFadeBottom(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    sizes: ScrollEdgeFadeDefaults.Sizes = ScrollEdgeFadeDefaults.sizes(),
    colors: ScrollEdgeFadeDefaults.Colors = ScrollEdgeFadeDefaults.colors(),
) {
    if (!visible) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(sizes.fadeHeight)
            .background(colors.bottomFadeBrush),
    )
}

@Composable
fun ScrollEdgeFadeContainer(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    sizes: ScrollEdgeFadeDefaults.Sizes = ScrollEdgeFadeDefaults.sizes(),
    colors: ScrollEdgeFadeDefaults.Colors = ScrollEdgeFadeDefaults.colors(),
    content: @Composable BoxScope.() -> Unit,
) {
    val showTop by remember { derivedStateOf { scrollState.canScrollBackward } }
    val showBottom by remember { derivedStateOf { scrollState.canScrollForward } }

    Box(modifier = modifier) {
        content()
        ScrollEdgeFadeTop(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = showTop,
            sizes = sizes,
            colors = colors,
        )
        ScrollEdgeFadeBottom(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showBottom,
            sizes = sizes,
            colors = colors,
        )
    }
}

@Preview
@Composable
private fun ScrollEdgeFadePreview() {
    val sizes = ScrollEdgeFadeDefaults.sizes()
    Box(modifier = Modifier.height(sizes.fadeHeight * 2)) {
        ScrollEdgeFadeTop()
        ScrollEdgeFadeBottom(modifier = Modifier.align(Alignment.BottomCenter))
    }
}
