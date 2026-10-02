package com.zero.fitness.ui.screens.workout.exercise

import android.content.ActivityNotFoundException
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zero.fitness.R
import com.zero.fitness.core.uuid.toUuidOrNull
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.resources.localizedDescription
import com.zero.fitness.ui.resources.localizedName
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppIconButton
import com.zero.fitness.ui.screens.common.Bubble
import com.zero.fitness.ui.screens.common.BubblesDefaults
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeContainer
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeDefaults
import com.zero.fitness.ui.theme.BorderLight
import com.zero.fitness.ui.theme.Coral
import com.zero.fitness.ui.theme.DarkPurple
import com.zero.fitness.ui.theme.Gray
import com.zero.fitness.ui.theme.Green
import com.zero.fitness.ui.theme.Purple
import com.zero.fitness.ui.theme.TextPrimary
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.Yellow
import com.zero.fitness.ui.theme.ZeroFitnessTheme
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToUpdate: (UUID) -> Unit,
    viewModel: ExerciseDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    ExerciseDetailsScreenContent(
        state = state,
        onNavigateUp = onNavigateUp,
        onNavigateToUpdate = onNavigateToUpdate,
    )
}

@Composable
private fun ExerciseDetailsScreenContent(
    state: ExerciseDetailsScreenState,
    onNavigateUp: () -> Unit,
    onNavigateToUpdate: (UUID) -> Unit,
) {
    val scrollFadeSizes = ScrollEdgeFadeDefaults.sizes()
    val scrollState = rememberScrollState()
    val canUpdate = !state.isLoading &&
        state.error == null &&
        state.exerciseId.toUuidOrNull() != null

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.exercise_details_app_bar_title),
                onBackClick = onNavigateUp,
                actionButton = {
                    if (canUpdate) {
                        AppIconButton(
                            icon = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.exercise_details_update),
                            onClick = {
                                state.exerciseId.toUuidOrNull()?.let(onNavigateToUpdate)
                            },
                        )
                    }
                },
            )
        },
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = stringResource(R.string.exercise_details_loading))
                    }
                }
                state.error != null && state.name.isBlank() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = exerciseDetailsErrorText(state.error))
                    }
                }
                else -> {
                    ScrollEdgeFadeContainer(
                        scrollState = scrollState,
                        modifier = Modifier.fillMaxSize(),
                        sizes = scrollFadeSizes,
                    ) {
                        ExerciseDetailsBody(
                            state = state,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(horizontal = 18.dp, vertical = 12.dp)
                                .padding(bottom = scrollFadeSizes.fadeHeight),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseDetailsBody(
    state: ExerciseDetailsScreenState,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val displayName = localizedName(state.name, state.nameSr, locale)
    val displayDescription = localizedDescription(
        state.description.ifBlank { null },
        state.descriptionSr.ifBlank { null },
        locale,
    )
    val equipmentLabel = state.equipment?.let { stringResource(it.labelRes()) }
    val typeLabel = state.selectedType?.let { stringResource(it.labelRes()) }
    val subtitle = listOfNotNull(equipmentLabel, typeLabel).joinToString(" · ")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Coral, Purple)))
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayName,
                        color = White,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (subtitle.isNotBlank()) {
                        Text(
                            text = subtitle,
                            color = White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
                val difficulty = state.difficulty
                if (difficulty != null) {
                    DifficultyBadge(level = difficulty)
                }
            }
            if (!displayDescription.isNullOrBlank()) {
                Text(
                    text = displayDescription,
                    color = White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }

        if (state.muscleGroups.isNotEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp,
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = stringResource(R.string.exercise_details_primary_muscles),
                        color = DarkPurple,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    FlowRow(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        val muscleColors = BubblesDefaults.bubbleColors(
                            backgroundSelected = Coral,
                            borderSelected = Coral,
                            labelSelected = White,
                        )
                        state.muscleGroups.forEach { group ->
                            Bubble(
                                label = stringResource(group.labelRes()),
                                isSelected = true,
                                colors = muscleColors,
                            )
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp,
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                Text(
                    text = stringResource(R.string.exercise_details_properties),
                    color = DarkPurple,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                )
                if (equipmentLabel != null) {
                    PropertyRow(
                        label = stringResource(R.string.exercise_details_equipment),
                        value = equipmentLabel,
                    )
                }
                PropertyRow(
                    label = stringResource(R.string.exercise_details_track_weight),
                    value = stringResource(
                        if (state.trackWeight) {
                            R.string.exercise_details_track_weight_yes
                        } else {
                            R.string.exercise_details_track_weight_no
                        },
                    ),
                    valueColor = if (state.trackWeight) Purple else TextPrimary,
                )
                if (typeLabel != null) {
                    PropertyRow(
                        label = stringResource(R.string.exercise_details_type),
                        value = typeLabel,
                        showDivider = state.videoUrl.isHttpUrl(),
                    )
                }
                if (state.videoUrl.isHttpUrl()) {
                    VideoUrlRow(url = state.videoUrl)
                }
            }
        }
    }
}

@Composable
private fun DifficultyBadge(level: DifficultyLevel) {
    val background = when (level) {
        DifficultyLevel.BEGINNER -> Green
        DifficultyLevel.INTERMEDIATE -> Yellow
        DifficultyLevel.ADVANCED -> Coral
        DifficultyLevel.ELITE -> DarkPurple
    }
    val labelColor = if (level == DifficultyLevel.INTERMEDIATE) DarkPurple else White
    Text(
        text = stringResource(level.labelRes()),
        color = labelColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

@Composable
private fun PropertyRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary,
    showDivider: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = Gray,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = value,
                color = valueColor,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderLight),
            )
        }
    }
}

@Composable
private fun VideoUrlRow(url: String) {
    val uriHandler = LocalUriHandler.current
    val openLabel = stringResource(R.string.exercise_details_open_video)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClickLabel = openLabel) {
                try {
                    uriHandler.openUri(url)
                } catch (_: IllegalArgumentException) {
                } catch (_: ActivityNotFoundException) {
                }
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.exercise_details_video_url),
            color = Gray,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = url,
            color = Purple,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f, fill = false),
        )
    }
}

@Composable
private fun exerciseDetailsErrorText(error: ExerciseDetailsError): String = when (error) {
    ExerciseDetailsError.NotFound -> stringResource(R.string.exercise_details_not_found)
    ExerciseDetailsError.Storage -> stringResource(R.string.exercise_details_error_storage)
    ExerciseDetailsError.Unknown -> stringResource(R.string.exercise_details_error_unknown)
}

private fun String.isHttpUrl(): Boolean =
    startsWith("http://") || startsWith("https://")

@Preview
@Composable
private fun Preview() {
    ZeroFitnessTheme(dynamicColor = false) {
        ExerciseDetailsScreenContent(
            state = ExerciseDetailsScreenState(
                exerciseId = "00000000-0000-0000-0000-000000000001",
                name = "Romanian Deadlift",
                description = "Hip-hinge movement keeping bar close to legs.",
                nameSr = "Rumunsko mrtvo dizanje",
                equipment = Equipment.BARBELL,
                selectedType = ExerciseType.COMPOUND,
                trackWeight = true,
                muscleGroups = setOf(
                    MuscleGroup.HAMSTRINGS,
                    MuscleGroup.GLUTES,
                    MuscleGroup.LOWER_BACK,
                ),
                difficulty = DifficultyLevel.INTERMEDIATE,
                videoUrl = "https://youtube.com/watch?v=example",
                isLoading = false,
            ),
            onNavigateUp = {},
            onNavigateToUpdate = {},
        )
    }
}
