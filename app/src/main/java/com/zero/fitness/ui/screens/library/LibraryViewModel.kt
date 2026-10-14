package com.zero.fitness.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.repository.ExerciseRepository
import com.zero.fitness.domain.sync.SyncPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val syncPolicy: SyncPolicy,
) : ViewModel() {

    private val isSyncing = MutableStateFlow(false)
    private val syncFailed = MutableStateFlow(false)
    private val selectedMuscleGroup = MutableStateFlow<MuscleGroup?>(null)

    private val catalog = combine(
        exerciseRepository.observeAll(),
        exerciseRepository.observePendingCount(),
        syncPolicy.observeCanSync(),
        isSyncing,
        syncFailed,
    ) { exercises, pendingCount, syncEnabled, syncing, failed ->
        LibraryCatalog(
            exercises = exercises,
            pendingCount = pendingCount,
            syncEnabled = syncEnabled,
            isSyncing = syncing,
            syncFailed = failed,
        )
    }

    val state = combine(
        catalog,
        selectedMuscleGroup,
    ) { catalog, selectedGroup ->
        val availableMuscleGroups = catalog.exercises.availableMuscleGroups()
        val effectiveGroup = selectedGroup.takeIf { it in availableMuscleGroups }
        LibraryScreenState(
            exercises = catalog.exercises.filter { exercise ->
                effectiveGroup == null || effectiveGroup in exercise.muscleGroups
            },
            pendingCount = catalog.pendingCount,
            syncEnabled = catalog.syncEnabled,
            isSyncing = catalog.isSyncing,
            syncFailed = catalog.syncFailed,
            selectedMuscleGroup = effectiveGroup,
            availableMuscleGroups = availableMuscleGroups,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        LibraryScreenState(),
    )

    fun setMuscleFilter(group: MuscleGroup?) {
        selectedMuscleGroup.value = group
    }

    fun sync() {
        if (isSyncing.value) return
        isSyncing.value = true
        syncFailed.value = false
        viewModelScope.launch {
            exerciseRepository.sync().onFailure { syncFailed.value = true }
            isSyncing.value = false
        }
    }

    fun delete(id: UUID) {
        viewModelScope.launch { exerciseRepository.delete(id) }
    }
}

data class LibraryScreenState(
    val exercises: List<Exercise> = emptyList(),
    val pendingCount: Int = 0,
    val syncEnabled: Boolean = false,
    val isSyncing: Boolean = false,
    val syncFailed: Boolean = false,
    val selectedMuscleGroup: MuscleGroup? = null,
    val availableMuscleGroups: List<MuscleGroup> = emptyList(),
) {
    val showSyncAction: Boolean
        get() = syncEnabled && (pendingCount > 0 || isSyncing || syncFailed)
}

private data class LibraryCatalog(
    val exercises: List<Exercise>,
    val pendingCount: Int,
    val syncEnabled: Boolean,
    val isSyncing: Boolean,
    val syncFailed: Boolean,
)

private fun List<Exercise>.availableMuscleGroups(): List<MuscleGroup> =
    MuscleGroup.entries.filter { group -> any { group in it.muscleGroups } }
