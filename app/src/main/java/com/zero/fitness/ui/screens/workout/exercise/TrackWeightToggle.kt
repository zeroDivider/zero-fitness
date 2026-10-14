package com.zero.fitness.ui.screens.workout.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.fitness.R
import com.zero.fitness.ui.screens.common.AppToggleDefaults
import com.zero.fitness.ui.screens.common.AppToggleSwitch
import com.zero.fitness.ui.theme.BorderLight
import com.zero.fitness.ui.theme.Gray
import com.zero.fitness.ui.theme.TextPrimary
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.ZeroFitnessTheme

object TrackWeightToggleDefaults {
    @Immutable
    data class SettingCardColors(
        val background: Color,
        val border: Color,
        val title: Color,
        val subtitle: Color,
    )

    @Composable
    fun settingCardColors(
        background: Color = White,
        border: Color = BorderLight,
        title: Color = TextPrimary,
        subtitle: Color = Gray,
    ): SettingCardColors = SettingCardColors(background, border, title, subtitle)

    @Immutable
    data class SettingCardSizes(
        val cornerRadius: Dp,
        val borderWidth: Dp,
        val contentPadding: Dp,
        val labelEndPadding: Dp,
        val subtitleTopPadding: Dp,
    )

    @Composable
    fun settingCardSizes(
        cornerRadius: Dp = 12.dp,
        borderWidth: Dp = 1.dp,
        contentPadding: Dp = 14.dp,
        labelEndPadding: Dp = 12.dp,
        subtitleTopPadding: Dp = 2.dp,
    ): SettingCardSizes = SettingCardSizes(
        cornerRadius,
        borderWidth,
        contentPadding,
        labelEndPadding,
        subtitleTopPadding,
    )
}

@Composable
private fun ToggleSettingCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cardColors: TrackWeightToggleDefaults.SettingCardColors =
        TrackWeightToggleDefaults.settingCardColors(),
    cardSizes: TrackWeightToggleDefaults.SettingCardSizes =
        TrackWeightToggleDefaults.settingCardSizes(),
    toggleColors: SwitchColors = AppToggleDefaults.colors(),
) {
    val cardShape = RoundedCornerShape(cardSizes.cornerRadius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(cardColors.background)
            .border(
                width = cardSizes.borderWidth,
                color = cardColors.border,
                shape = cardShape,
            )
            .padding(cardSizes.contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = cardSizes.labelEndPadding)
                .clearAndSetSemantics { },
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = cardColors.title,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = cardColors.subtitle,
                modifier = Modifier.padding(top = cardSizes.subtitleTopPadding),
            )
        }
        AppToggleSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            switchLabel = title,
            colors = toggleColors,
        )
    }
}

@Composable
fun TrackWeightToggleSettingCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cardColors: TrackWeightToggleDefaults.SettingCardColors =
        TrackWeightToggleDefaults.settingCardColors(),
    cardSizes: TrackWeightToggleDefaults.SettingCardSizes =
        TrackWeightToggleDefaults.settingCardSizes(),
    toggleColors: SwitchColors = AppToggleDefaults.colors(),
) {
    ToggleSettingCard(
        title = stringResource(R.string.exercise_track_weight_title),
        subtitle = stringResource(R.string.exercise_track_weight_subtitle),
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        cardColors = cardColors,
        cardSizes = cardSizes,
        toggleColors = toggleColors,
    )
}

@Preview(name = "Track weight toggle — on")
@Composable
private fun TrackWeightToggleOnPreview() {
    ZeroFitnessTheme(dynamicColor = false) {
        TrackWeightToggleSettingCard(
            modifier = Modifier.padding(16.dp),
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Preview(name = "Track weight toggle — off")
@Composable
private fun TrackWeightToggleOffPreview() {
    ZeroFitnessTheme(dynamicColor = false) {
        TrackWeightToggleSettingCard(
            modifier = Modifier.padding(16.dp),
            checked = false,
            onCheckedChange = {},
        )
    }
}

@Preview(name = "Track weight toggle — disabled")
@Composable
private fun TrackWeightToggleDisabledPreview() {
    ZeroFitnessTheme(dynamicColor = false) {
        TrackWeightToggleSettingCard(
            modifier = Modifier.padding(16.dp),
            checked = true,
            enabled = false,
            onCheckedChange = {},
        )
    }
}
