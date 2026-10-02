package com.zero.fitness.ui.screens.workout.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zero.fitness.R
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.screens.common.BubblesDefaults
import com.zero.fitness.ui.screens.common.InteractiveBubble
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeContainer
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeDefaults
import com.zero.fitness.ui.theme.Coral
import com.zero.fitness.ui.theme.DarkPurple
import com.zero.fitness.ui.theme.Gray
import com.zero.fitness.ui.theme.Green
import com.zero.fitness.ui.theme.LightGray
import com.zero.fitness.ui.theme.Transparent
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.Yellow
import com.zero.fitness.ui.theme.ZeroFitnessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExerciseScreen(
    viewModel: NewExerciseViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is NewExerciseEvent.Saved) onNavigateUp()
        }
    }

    NewExerciseScreenContent(
        state = state,
        onNameChange = viewModel::updateName,
        onDescriptionChange = viewModel::updateDescription,
        onNameSrChange = viewModel::updateNameSr,
        onDescriptionSrChange = viewModel::updateDescriptionSr,
        onEquipmentChange = viewModel::updateEquipment,
        onExerciseTypeChange = viewModel::updateExerciseType,
        onTrackWeightChange = viewModel::updateTrackWeight,
        onMuscleGroupToggle = viewModel::toggleMuscleGroup,
        onDifficultyChange = viewModel::updateDifficulty,
        onVideoUrlChange = viewModel::updateVideoUrl,
        onNavigateUp = onNavigateUp,
        onSave = viewModel::save
    )
}

@Composable
private fun NewExerciseScreenContent(
    state: NewExerciseScreenState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNameSrChange: (String) -> Unit,
    onDescriptionSrChange: (String) -> Unit,
    onEquipmentChange: (Equipment) -> Unit,
    onExerciseTypeChange: (ExerciseType) -> Unit,
    onTrackWeightChange: (Boolean) -> Unit,
    onMuscleGroupToggle: (MuscleGroup) -> Unit,
    onDifficultyChange: (DifficultyLevel) -> Unit,
    onVideoUrlChange: (String) -> Unit,
    onNavigateUp: () -> Unit,
    onSave: () -> Unit,
) {
    val scrollFadeSizes = ScrollEdgeFadeDefaults.sizes()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(
                    if (state.isEdit) {
                        R.string.new_exercise_app_bar_title_edit
                    } else {
                        R.string.new_exercise_app_bar_title
                    },
                ),
                subtitle = stringResource(
                    if (state.isEdit) {
                        R.string.new_exercise_app_bar_subtitle_edit
                    } else {
                        R.string.new_exercise_app_bar_subtitle
                    },
                ),
                onBackClick = onNavigateUp,
            )
        },
        bottomBar = {
            Column {
                AnimatedVisibility(visible = state.error != null) {
                    val error = state.error
                    Text(
                        text = if (error == null) "" else newExerciseErrorText(error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                    )
                }
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .padding(top = 14.dp, bottom = 12.dp),
                    label = stringResource(R.string.new_exercise_save),
                    colors = AppButtonDefaults.buttonColors(),
                    isEnabled = !state.isSaving && !state.isLoading,
                    onClickAction = onSave,
                )
            }
        },
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding),
        ) {
            ScrollEdgeFadeContainer(
                scrollState = scrollState,
                modifier = Modifier.fillMaxSize(),
                sizes = scrollFadeSizes,
            ) {
                NewExerciseForm(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                        .padding(bottom = scrollFadeSizes.fadeHeight),
                    state = state,
                    onNameChange = onNameChange,
                    onDescriptionChange = onDescriptionChange,
                    onNameSrChange = onNameSrChange,
                    onDescriptionSrChange = onDescriptionSrChange,
                    onEquipmentChange = onEquipmentChange,
                    onExerciseTypeChange = onExerciseTypeChange,
                    onTrackWeightChange = onTrackWeightChange,
                    onMuscleGroupToggle = onMuscleGroupToggle,
                    onDifficultyChange = onDifficultyChange,
                    onVideoUrlChange = onVideoUrlChange,
                )
            }
        }
    }
}

@Composable
private fun NewExerciseForm(
    state: NewExerciseScreenState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onNameSrChange: (String) -> Unit,
    onDescriptionSrChange: (String) -> Unit,
    onEquipmentChange: (Equipment) -> Unit,
    onExerciseTypeChange: (ExerciseType) -> Unit,
    onTrackWeightChange: (Boolean) -> Unit,
    onMuscleGroupToggle: (MuscleGroup) -> Unit,
    onDifficultyChange: (DifficultyLevel) -> Unit,
    onVideoUrlChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val muscleBubbleColors = BubblesDefaults.bubbleColors(
        backgroundSelected = Coral,
        borderSelected = Coral,
    )
    val compactBubbleSize = BubblesDefaults.bubbleSize(
        borderRadius = 8.dp,
        borderWidth = 0.dp,
        verticalPadding = 8.dp,
        horizontalPadding = 4.dp,
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.new_exercise_label_name_sr),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.nameSr,
            label = { Text(stringResource(R.string.new_exercise_hint_name_sr)) },
            onValueChange = onNameSrChange,
        )
        Text(
            text = stringResource(R.string.new_exercise_label_description_sr),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.descriptionSr,
            minLines = 4,
            label = { Text(stringResource(R.string.new_exercise_hint_description_sr)) },
            onValueChange = onDescriptionSrChange,
        )
        Text(
            text = stringResource(R.string.new_exercise_label_name),
            style = MaterialTheme.typography.titleSmall,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.name,
            label = { Text(stringResource(R.string.new_exercise_hint_name)) },
            onValueChange = onNameChange,
        )
        Text(
            text = stringResource(R.string.new_exercise_label_description),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.description,
            minLines = 4,
            label = { Text(stringResource(R.string.new_exercise_hint_description)) },
            onValueChange = onDescriptionChange,
        )
        Text(
            text = stringResource(R.string.new_exercise_label_equipment),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Equipment.entries.forEach { equipment ->
                InteractiveBubble(
                    label = stringResource(equipment.labelRes()),
                    isSelected = equipment == state.equipment,
                    onClickAction = { onEquipmentChange(equipment) },
                )
            }
        }
        Text(
            text = stringResource(R.string.new_exercise_label_exercise_type),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ExerciseType.entries.forEach { exerciseType ->
                InteractiveBubble(
                    label = stringResource(exerciseType.labelRes()),
                    isSelected = exerciseType == state.selectedType,
                    onClickAction = { onExerciseTypeChange(exerciseType) },
                )
            }
        }
        TrackWeightToggleSettingCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            checked = state.trackWeight,
            onCheckedChange = onTrackWeightChange,
        )
        Text(
            text = stringResource(R.string.new_exercise_label_muscle_groups),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MuscleGroup.entries.forEach { group ->
                InteractiveBubble(
                    label = stringResource(group.labelRes()),
                    isSelected = group in state.muscleGroups,
                    colors = muscleBubbleColors,
                    onClickAction = { onMuscleGroupToggle(group) },
                )
            }
        }
        Text(
            text = stringResource(R.string.new_exercise_label_difficulty),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            DifficultyLevel.entries.forEach { level ->
                InteractiveBubble(
                    modifier = Modifier.weight(1f),
                    label = stringResource(level.labelRes()),
                    isSelected = level == state.difficulty,
                    colors = difficultyLevelBubbleColors(level),
                    sizes = compactBubbleSize,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    onClickAction = { onDifficultyChange(level) },
                )
            }
        }
        Text(
            text = stringResource(R.string.new_exercise_label_video_url),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 14.dp),
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.videoUrl,
            label = { Text(stringResource(R.string.new_exercise_hint_video_url)) },
            onValueChange = onVideoUrlChange,
        )
    }
}

@Composable
private fun difficultyLevelBubbleColors(
    level: DifficultyLevel,
): BubblesDefaults.BubbleColors = BubblesDefaults.bubbleColors(
    background = LightGray,
    backgroundSelected = when (level) {
        DifficultyLevel.BEGINNER -> Green
        DifficultyLevel.INTERMEDIATE -> Yellow
        DifficultyLevel.ADVANCED -> Coral
        DifficultyLevel.ELITE -> DarkPurple
    },
    border = Transparent,
    borderSelected = Transparent,
    label = Gray,
    labelSelected = if (level == DifficultyLevel.INTERMEDIATE) DarkPurple else White,
)

@Composable
private fun newExerciseErrorText(error: NewExerciseError): String = when (error) {
    NewExerciseError.NameRequired -> stringResource(R.string.new_exercise_error_name_required)
    NewExerciseError.EquipmentRequired -> stringResource(R.string.new_exercise_error_equipment_required)
    NewExerciseError.TypeRequired -> stringResource(R.string.new_exercise_error_type_required)
    NewExerciseError.VideoUrlInvalid -> stringResource(R.string.new_exercise_error_video_url_invalid)
    NewExerciseError.NotFound -> stringResource(R.string.new_exercise_error_not_found)
    NewExerciseError.Storage -> stringResource(R.string.new_exercise_error_storage)
    NewExerciseError.Unknown -> stringResource(R.string.new_exercise_error_unknown)
}

@Preview
@Composable
private fun Preview() {
    ZeroFitnessTheme(dynamicColor = false) {
        NewExerciseScreenContent(
            state = NewExerciseScreenState(
                name = "Barbell squat",
                description = "Sit back and stand up with the bar on your back.",
                nameSr = "Čučanj sa šipkom",
                descriptionSr = "Sedni unazad i ustani sa šipkom na leđima.",
                equipment = Equipment.BARBELL,
                selectedType = ExerciseType.COMPOUND,
                trackWeight = true,
                muscleGroups = setOf(MuscleGroup.QUADS, MuscleGroup.GLUTES),
                difficulty = DifficultyLevel.INTERMEDIATE,
                videoUrl = "https://youtube.com/watch?v=example",
            ),
            onNameChange = {},
            onDescriptionChange = {},
            onNameSrChange = {},
            onDescriptionSrChange = {},
            onEquipmentChange = {},
            onExerciseTypeChange = {},
            onTrackWeightChange = {},
            onMuscleGroupToggle = {},
            onDifficultyChange = {},
            onVideoUrlChange = {},
            onNavigateUp = {},
            onSave = {},
        )
    }
}
