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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.InteractiveBubble
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExerciseScreen(
    onNavigateUp: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var trackWeight by remember { mutableStateOf(false) }

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
                    value = name,
                    label = { Text("Enter name") },
                    onValueChange = { },
                )
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 14.dp),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    minLines = 4,
                    label = { Text("Enter some description") },
                    onValueChange = { },
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
                        InteractiveBubble(label = it.name) {

                        }
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
                            //isSelected = it == state.selectedType
                        ) {

                        }
                    }
                }
                TrackWeightToggleSettingCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    checked = trackWeight,
                    onCheckedChange = { trackWeight = it },
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
