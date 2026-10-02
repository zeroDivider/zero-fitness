package com.zero.fitness.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.theme.DarkPurple
import com.zero.fitness.ui.theme.Transparent
import com.zero.fitness.ui.theme.White

object BubblesDefaults {
    @Immutable
    data class BubbleColors(
        val background: Color,
        val backgroundSelected: Color,
        val border: Color,
        val borderSelected: Color,
        val label: Color,
        val labelSelected: Color,
    )

    @Composable
    fun bubbleColors(
        background: Color = Transparent,
        backgroundSelected: Color = DarkPurple,
        border: Color = Color.LightGray,
        borderSelected: Color = DarkPurple,
        label: Color = Color.DarkGray,
        labelSelected: Color = White,
    ): BubbleColors =
        BubbleColors(background, backgroundSelected, border, borderSelected, label, labelSelected)

    @Immutable
    data class BubbleSizes(
        val borderRadius: Dp,
        val borderWidth: Dp,
        val verticalPadding: Dp,
        val horizontalPadding: Dp,
    )

    @Composable
    fun bubbleSize(
        borderRadius: Dp = 25.dp,
        borderWidth: Dp = 1.dp,
        verticalPadding: Dp = 4.dp,
        horizontalPadding: Dp = 10.dp,
    ): BubbleSizes = BubbleSizes(borderRadius, borderWidth, verticalPadding, horizontalPadding)
}

@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    colors: BubblesDefaults.BubbleColors = BubblesDefaults.bubbleColors(),
    sizes: BubblesDefaults.BubbleSizes = BubblesDefaults.bubbleSize(),
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
) = InteractiveBubble(
    modifier = modifier,
    label = label,
    isSelected = isSelected,
    colors = colors,
    sizes = sizes,
    style = style,
    maxLines = maxLines,
) { /* keep empty */ }

@Composable
fun InteractiveBubble(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    colors: BubblesDefaults.BubbleColors = BubblesDefaults.bubbleColors(),
    sizes: BubblesDefaults.BubbleSizes = BubblesDefaults.bubbleSize(),
    style: TextStyle = LocalTextStyle.current,
    maxLines: Int = Int.MAX_VALUE,
    onClickAction: () -> Unit,
) {
    val shape = RoundedCornerShape(sizes.borderRadius)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClickAction,
            )
            .background(
                color = if (isSelected) colors.backgroundSelected else colors.background,
                shape = shape,
            )
            .border(
                width = sizes.borderWidth,
                color = if (isSelected) colors.borderSelected else colors.border,
                shape = shape,
            )
            .padding(horizontal = sizes.horizontalPadding, vertical = sizes.verticalPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.labelSelected else colors.label,
            style = style,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun BubblePreview() {
    Bubble(label = "Zhex")
}

@Preview
@Composable
private fun BubbleSelectedPreview() {
    Bubble(label = "Zhivanovich", isSelected = true)
}
