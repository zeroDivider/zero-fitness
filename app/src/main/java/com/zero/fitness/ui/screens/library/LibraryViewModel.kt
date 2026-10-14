package com.zero.fitness.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero.fitness.domain.model.Exercise
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

    val state = combine(
        exerciseRepository.observeAll(),
        exerciseRepository.observePendingCount(),
        syncPolicy.observeCanSync(),
        isSyncing,
        syncFailed,
    ) { exercises, pendingCount, syncEnabled, syncing, failed ->
        LibraryScreenState(
            exercises = exercises,
            pendingCount = pendingCount,
            syncEnabled = syncEnabled,
            isSyncing = syncing,
            syncFailed = failed,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        LibraryScreenState(),
    )

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
) {
    val showSyncAction: Boolean
        get() = syncEnabled && (pendingCount > 0 || isSyncing || syncFailed)
}
