package com.zero.fitness.ui.screens.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.ui.theme.Coral
import com.zero.fitness.ui.theme.DarkPurple
import com.zero.fitness.ui.theme.Purple
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.Yellow

object AppButtonDefaults {
    @Immutable
    data class ButtonColors(
        val background: Color,
        val label: Color,
    )

    @Composable
    fun buttonColors(
        background: Color = Purple,
        label: Color = White,
    ): ButtonColors = ButtonColors(background, label)
}

@Composable
fun AppButton(
    label: String,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    colors: AppButtonDefaults.ButtonColors = AppButtonDefaults.buttonColors(),
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClickAction,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.background,
            contentColor = colors.label,
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(label)
    }
}

@Preview
@Composable
private fun LePreview() {
    Column(
        modifier = Modifier.width(IntrinsicSize.Max),
    ) {
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Save workout",
            onClickAction = {},
        )
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            label = "+ Add Exercise",
            onClickAction = {},
            colors = AppButtonDefaults.buttonColors(
                background = Yellow,
                label = DarkPurple,
            ),
        )
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Complete all",
            onClickAction = {},
            colors = AppButtonDefaults.buttonColors(
                background = Coral,
                label = White,
            ),
        )
    }
}
