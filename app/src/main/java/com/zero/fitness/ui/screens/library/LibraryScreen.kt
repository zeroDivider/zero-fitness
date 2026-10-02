package com.zero.fitness.ui.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zero.fitness.R
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults

@Composable
fun LibraryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToNewExercise: () -> Unit,
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.library_app_bar_title),
                subtitle = stringResource(R.string.library_app_bar_subtitle),
                onBackClick = { onNavigateUp() },
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
                if (state.syncEnabled) {
                    LibrarySyncStatus(
                        state = state,
                        onSync = viewModel::sync,
                    )
                }
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
                            LibraryExerciseRow(exercise)
                        }
                    }
                }
                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.library_add_exercise),
                    colors = AppButtonDefaults.buttonColors(),
                    onClickAction = { onNavigateToNewExercise() }
                )
            }
        }
    }
}

@Composable
private fun LibrarySyncStatus(
    state: LibraryScreenState,
    onSync: () -> Unit,
) {
    when {
        state.isSyncing -> Text(
            text = stringResource(R.string.library_syncing),
            modifier = Modifier.padding(vertical = 8.dp),
        )
        state.syncFailed -> Text(
            text = stringResource(R.string.library_sync_failed),
            modifier = Modifier
                .clickable(onClick = onSync)
                .padding(vertical = 8.dp),
        )
        state.pendingCount > 0 -> Text(
            text = stringResource(R.string.library_pending_count, state.pendingCount),
            modifier = Modifier
                .clickable(onClick = onSync)
                .padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun LibraryExerciseRow(exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
    LibraryScreen({}, {})
}
