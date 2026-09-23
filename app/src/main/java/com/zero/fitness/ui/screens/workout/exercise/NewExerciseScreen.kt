package com.zero.fitness.ui.screens.workout.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.screens.common.InteractiveBubble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExerciseScreen(
    viewModel: NewExerciseViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBar(
                title = "New exercise",
                subtitle = "Go GO GOOO!",
                onBackClick = onNavigateUp,
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleSmall,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.name,
                    label = { Text("Enter name") },
                    onValueChange = { viewModel.updateName(it) },
                )
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 14.dp),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.description,
                    minLines = 4,
                    label = { Text("Enter some description") },
                    onValueChange = { viewModel.updateDescription(it) },
                )
                Text(
                    text = "Equipment",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 14.dp),
                )
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Equipment.entries.forEach {
                        InteractiveBubble(
                            label = it.name,
                            isSelected = it == state.equipment
                        ) { viewModel.updateEquipment(it) }
                    }
                }
                Text(
                    text = "Exercise Type",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 14.dp),
                )
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ExerciseType.entries.forEach {
                        InteractiveBubble(
                            label = it.name,
                            isSelected = it == state.selectedType,
                        ) { viewModel.updateExerciseType(it) }
                    }
                }
                TrackWeightToggleSettingCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    checked = state.trackWeight,
                    onCheckedChange = { viewModel.updateTrackWeight(it) },
                )
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    label = "Save Exercise",
                    colors = AppButtonDefaults.buttonColors(),
                    onClickAction = { }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NewExerciseScreen {}
}
