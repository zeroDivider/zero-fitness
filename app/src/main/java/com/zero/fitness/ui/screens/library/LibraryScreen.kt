package com.zero.fitness.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.ui.resources.labelRes
import com.zero.fitness.ui.screens.common.AppBar
import com.zero.fitness.ui.screens.common.AppButton
import com.zero.fitness.ui.screens.common.AppButtonDefaults
import com.zero.fitness.ui.screens.common.InteractiveBubble
import com.zero.fitness.ui.theme.Amber
import com.zero.fitness.ui.theme.White
import com.zero.fitness.ui.theme.ZeroFitnessTheme
import kotlinx.coroutines.delay
import java.util.UUID

private const val SEARCH_DEBOUNCE_MS = 250L

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
        onSearchQueryChange = viewModel::setSearchQuery,
        onMuscleFilterChange = viewModel::setMuscleFilter,
        onSync = viewModel::sync,
    )
}

@Composable
private fun LibraryScreenContent(
    state: LibraryScreenState,
    onNavigateUp: () -> Unit,
    onNavigateToCreateExercise: () -> Unit,
    onNavigateToExerciseDetails: (UUID) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onMuscleFilterChange: (MuscleGroup?) -> Unit,
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
                            pendingCount = state.pendingCount,
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
                LibrarySearchField(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChange,
                )
                if (state.showMuscleFilter) {
                    LibraryMuscleFilterRow(
                        selectedMuscleGroup = state.selectedMuscleGroup,
                        availableMuscleGroups = state.availableMuscleGroups,
                        onMuscleFilterChange = onMuscleFilterChange,
                    )
                }
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    state.exercises.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = stringResource(R.string.library_empty))
                        }
                    }
                    else -> {
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
    pendingCount: Int,
    isSyncing: Boolean,
    syncFailed: Boolean,
    onSync: () -> Unit,
) {
    val contentDescription = when {
        isSyncing -> stringResource(R.string.library_sync_syncing)
        syncFailed -> stringResource(R.string.library_sync_failed_action)
        else -> stringResource(R.string.library_sync_action, pendingCount)
    }
    BadgedBox(
        badge = {
            if (pendingCount > 0 && !isSyncing && !syncFailed) {
                Badge(
                    containerColor = Amber,
                    contentColor = White,
                ) {
                    Text(
                        text = if (pendingCount > 99) "99+" else "$pendingCount",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        },
    ) {
        IconButton(
            onClick = onSync,
            enabled = !isSyncing,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = White.copy(alpha = 0.3f))
                .semantics { this.contentDescription = contentDescription },
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
}

@Composable
private fun LibrarySearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    var draftQuery by remember { mutableStateOf(query) }
    LaunchedEffect(draftQuery) {
        delay(SEARCH_DEBOUNCE_MS)
        if (draftQuery != query) onQueryChange(draftQuery)
    }
    LaunchedEffect(query) {
        if (query != draftQuery) draftQuery = query
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        value = draftQuery,
        onValueChange = { draftQuery = it },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        placeholder = { Text(stringResource(R.string.library_search_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.library_search_hint),
            )
        },
    )
}

@Composable
private fun LibraryMuscleFilterRow(
    selectedMuscleGroup: MuscleGroup?,
    availableMuscleGroups: List<MuscleGroup>,
    onMuscleFilterChange: (MuscleGroup?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(top = 12.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InteractiveBubble(
            label = stringResource(R.string.library_filter_all),
            isSelected = selectedMuscleGroup == null,
            onClickAction = { onMuscleFilterChange(null) },
        )
        availableMuscleGroups.forEach { group ->
            InteractiveBubble(
                label = stringResource(group.labelRes()),
                isSelected = group == selectedMuscleGroup,
                onClickAction = { onMuscleFilterChange(group) },
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleSmall,
            )
            if (exercise.syncPending) {
                val notSynced = stringResource(R.string.library_exercise_not_synced)
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(color = Amber, shape = CircleShape)
                        .semantics { contentDescription = notSynced },
                )
            }
        }
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
                isLoading = false,
                exercises = listOf(
                    Exercise(
                        id = UUID.randomUUID(),
                        name = "Bench Press",
                        nameSr = "Benč",
                        equipment = Equipment.BARBELL,
                        muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS),
                        updatedAtEpochMs = 0L,
                    ),
                    Exercise(
                        id = UUID.randomUUID(),
                        name = "Pull-up",
                        nameSr = "Zgib",
                        equipment = Equipment.BODYWEIGHT,
                        muscleGroups = listOf(MuscleGroup.LATS, MuscleGroup.BICEPS),
                        updatedAtEpochMs = 0L,
                        syncPending = true,
                    ),
                ),
                pendingCount = 1,
                syncEnabled = true,
                availableMuscleGroups = listOf(
                    MuscleGroup.CHEST,
                    MuscleGroup.TRICEPS,
                    MuscleGroup.LATS,
                    MuscleGroup.BICEPS,
                ),
            ),
            onNavigateUp = {},
            onNavigateToCreateExercise = {},
            onNavigateToExerciseDetails = {},
            onSearchQueryChange = {},
            onMuscleFilterChange = {},
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
                pendingCount = 3,
                isLoading = false,
                syncEnabled = true,
                isSyncing = true,
            ),
            onNavigateUp = {},
            onNavigateToCreateExercise = {},
            onNavigateToExerciseDetails = {},
            onSearchQueryChange = {},
            onMuscleFilterChange = {},
            onSync = {},
        )
    }
}

@Composable
@Preview
private fun PreviewLoading() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibraryScreenContent(
            state = LibraryScreenState(isLoading = true),
            onNavigateUp = {},
            onNavigateToCreateExercise = {},
            onNavigateToExerciseDetails = {},
            onSearchQueryChange = {},
            onMuscleFilterChange = {},
            onSync = {},
        )
    }
}

@Composable
@Preview(name = "Sync Action — pending badge")
private fun PreviewSyncActionPending() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibrarySyncAction(
            pendingCount = 3,
            isSyncing = false,
            syncFailed = false,
            onSync = {},
        )
    }
}

@Composable
@Preview(name = "Sync Action — syncing spinner")
private fun PreviewSyncActionSyncing() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibrarySyncAction(
            pendingCount = 3,
            isSyncing = true,
            syncFailed = false,
            onSync = {},
        )
    }
}

@Composable
@Preview(name = "Sync Action — failed")
private fun PreviewSyncActionFailed() {
    ZeroFitnessTheme(dynamicColor = false) {
        LibrarySyncAction(
            pendingCount = 0,
            isSyncing = false,
            syncFailed = true,
            onSync = {},
        )
    }
}
