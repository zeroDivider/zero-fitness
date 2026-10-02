package com.zero.fitness.ui.screens.workout.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.screens.common.InteractiveBubble
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeContainer
import com.zero.fitness.ui.screens.common.ScrollEdgeFadeDefaults

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

    val scrollFadeSizes = ScrollEdgeFadeDefaults.sizes()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.new_exercise_app_bar_title),
                subtitle = stringResource(R.string.new_exercise_app_bar_subtitle),
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
                    isEnabled = !state.isSaving,
                    onClickAction = viewModel::save,
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
                    onNameChange = viewModel::updateName,
                    onDescriptionChange = viewModel::updateDescription,
                    onNameSrChange = viewModel::updateNameSr,
                    onDescriptionSrChange = viewModel::updateDescriptionSr,
                    onEquipmentChange = viewModel::updateEquipment,
                    onExerciseTypeChange = viewModel::updateExerciseType,
                    onTrackWeightChange = viewModel::updateTrackWeight,
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
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
    }
}

@Composable
private fun newExerciseErrorText(error: NewExerciseError): String = when (error) {
    NewExerciseError.NameRequired -> stringResource(R.string.new_exercise_error_name_required)
    NewExerciseError.EquipmentRequired -> stringResource(R.string.new_exercise_error_equipment_required)
    NewExerciseError.TypeRequired -> stringResource(R.string.new_exercise_error_type_required)
    NewExerciseError.Storage -> stringResource(R.string.new_exercise_error_storage)
    NewExerciseError.Unknown -> stringResource(R.string.new_exercise_error_unknown)
}

@Preview
@Composable
private fun Preview() {
    NewExerciseScreen {}
}
