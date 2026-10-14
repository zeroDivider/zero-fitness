package com.zero.fitness.ui.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zero.fitness.R
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.ZeroFitnessTheme
import java.util.UUID

@Composable
fun LibraryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToExerciseDetails: (UUID) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LibraryScreenContent(
        state = state,
        onNavigateUp = onNavigateUp,
        onNavigateToCreateExercise = onNavigateToCreateExercise,
        onNavigateToExerciseDetails = onNavigateToExerciseDetails,
        onSync = viewModel::sync,
    )
}

@Composable
private fun LibraryScreenContent(
    state: LibraryScreenState,
    onNavigateUp: () -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToExerciseDetails: (UUID) -> Unit,
    onSync: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.library_app_bar_title),
                subtitle = stringResource(R.string.library_app_bar_subtitle),
                onBackClick = onNavigateUp,
                actionButton = {
                    if (state.showSyncAction) {
                        LibrarySyncAction(
                            isSyncing = state.isSyncing,
                            syncFailed = state.syncFailed,
                            onSync = onSync,
                        )
                    }
                },
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
            ) {
                if (state.exercises.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = stringResource(R.string.library_empty))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {
                        items(state.exercises, key = { it.id }) { exercise ->
                            LibraryExerciseRow(
                                exercise = exercise,
                                onClick = { onNavigateToExerciseDetails(exercise.id) },
                            )
                        }
                    }
                }
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.library_add_exercise),
                    colors = AppButtonDefaults.buttonColors(),
                    onClickAction = onNavigateToCreateExercise,
                )
            }
        }
    }
}

@Composable
private fun LibrarySyncAction(
    isSyncing: Boolean,
    syncFailed: Boolean,
    onSync: () -> Unit,
) {
    val contentDescription = stringResource(
        when {
            isSyncing -> R.string.library_sync_syncing
            syncFailed -> R.string.library_sync_failed_action
            else -> R.string.library_sync_action
        },
    )
    IconButton(
        onClick = onSync,
        enabled = !isSyncing,
        modifier = Modifier.semantics { this.contentDescription = contentDescription },
    ) {
        if (isSyncing) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = White,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = if (syncFailed) {
                    MaterialTheme.colorScheme.error
                } else {
                    White
                },
            )
        }
    }
}

@Composable
private fun LibraryExerciseRow(
    exercise: Exercise,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.titleSmall,
        )
        val equipment = exercise.equipment
        if (equipment != null) {
            Text(
                text = stringResource(equipment.labelRes()),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibraryScreenContent(
            state = LibraryScreenState(
                exercises = listOf(
                    Exercise(
                        id = UUID.randomUUID(),
                        name = "Bench Press",
                        equipment = Equipment.BARBELL,
                        updatedAtEpochMs = 0L,
                    ),
                ),
                pendingCount = 1,
                syncEnabled = true,
            ),
            onNavigateUp = {},
            onNavigateToCreateExercise = {},
            onNavigateToExerciseDetails = {},
            onSync = {},
        )
    }
}

@Composable
@Preview
private fun PreviewEmptySyncing() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibraryScreenContent(
            state = LibraryScreenState(
                syncEnabled = true,
                isSyncing = true,
            ),
            onNavigateUp = {},
            onNavigateToCreateExercise = {},
            onNavigateToExerciseDetails = {},
            onSync = {},
        )
    }
}
