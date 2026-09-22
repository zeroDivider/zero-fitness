package com.zero.fitness.ui.screens.common

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.zero.fitness.R
import com.zero.fitness.ui.theme.Purple
import com.zero.fitness.ui.theme.ToggleTrackOff
import com.zero.fitness.ui.theme.White

object AppToggleDefaults {
    @Composable
    fun colors(
        checkedTrackColor: Color = Purple,
        checkedThumbColor: Color = White,
        uncheckedTrackColor: Color = ToggleTrackOff,
        uncheckedThumbColor: Color = White,
        uncheckedBorderColor: Color = Color.Transparent,
    ): SwitchColors = SwitchDefaults.colors(
        checkedTrackColor = checkedTrackColor,
        checkedThumbColor = checkedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedBorderColor = uncheckedBorderColor,
    )
}

@Composable
fun AppToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    switchLabel: String? = null,
    colors: SwitchColors = AppToggleDefaults.colors(),
) {
    val toggleStateDescription = stringResource(
        if (checked) R.string.toggle_state_on else R.string.toggle_state_off,
    )

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = colors,
        modifier = modifier.semantics {
            stateDescription = toggleStateDescription
            switchLabel?.let { contentDescription = it }
        },
    )
}
