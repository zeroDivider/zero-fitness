package com.zero.fitness.ui.screens.workout.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero.fitness.core.result.AppError
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NewExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val exerciseId = UUID.randomUUID()

    private val _viewState = MutableStateFlow(NewExerciseScreenState())
    val viewState = _viewState.asStateFlow()

    private val _events = Channel<NewExerciseEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun updateName(newName: String) {
        _viewState.update { it.copy(name = newName, error = null) }
    }

    fun updateDescription(newDescription: String) {
        _viewState.update { it.copy(description = newDescription, error = null) }
    }

    fun updateNameSr(newNameSr: String) {
        _viewState.update { it.copy(nameSr = newNameSr, error = null) }
    }

    fun updateDescriptionSr(newDescriptionSr: String) {
        _viewState.update { it.copy(descriptionSr = newDescriptionSr, error = null) }
    }

    fun updateEquipment(equipment: Equipment) {
        _viewState.update { it.copy(equipment = equipment, error = null) }
    }

    fun updateExerciseType(type: ExerciseType) {
        _viewState.update { it.copy(selectedType = type, error = null) }
    }

    fun updateTrackWeight(trackWeight: Boolean) {
        _viewState.update { it.copy(trackWeight = trackWeight, error = null) }
    }

    fun toggleMuscleGroup(group: MuscleGroup) {
        _viewState.update { state ->
            val next = state.muscleGroups.toMutableSet()
            if (!next.add(group)) next.remove(group)
            state.copy(muscleGroups = next, error = null)
        }
    }

    fun updateDifficulty(level: DifficultyLevel) {
        _viewState.update { it.copy(difficulty = level, error = null) }
    }

    fun updateVideoUrl(videoUrl: String) {
        _viewState.update { it.copy(videoUrl = videoUrl, error = null) }
    }

    fun save() {
        if (_viewState.value.isSaving) return
        _viewState.update { it.copy(isSaving = true, error = null) }
        val state = _viewState.value
        val validationError = when {
            state.name.isBlank() -> NewExerciseError.NameRequired
            state.equipment == null -> NewExerciseError.EquipmentRequired
            state.selectedType == null -> NewExerciseError.TypeRequired
            state.videoUrl.isNotBlank() && !state.videoUrl.trim().isHttpUrl() ->
                NewExerciseError.VideoUrlInvalid
            else -> null
        }
        if (validationError != null) {
            _viewState.update { it.copy(isSaving = false, error = validationError) }
            return
        }
        val exercise = state.toExercise(exerciseId)
        viewModelScope.launch {
            exerciseRepository.save(exercise)
                .onSuccess { _events.send(NewExerciseEvent.Saved) }
                .onFailure { error ->
                    _viewState.update {
                        it.copy(
                            isSaving = false,
                            error = if (error is AppError.Storage) {
                                NewExerciseError.Storage
                            } else {
                                NewExerciseError.Unknown
                            },
                        )
                    }
                }
        }
    }
}

private fun String.isHttpUrl(): Boolean =
    startsWith("http://") || startsWith("https://")

private fun NewExerciseScreenState.toExercise(id: UUID) = Exercise(
    id = id,
    name = name.trim(),
    description = description.trim().ifBlank { null },
    nameSr = nameSr.trim().ifBlank { null },
    descriptionSr = descriptionSr.trim().ifBlank { null },
    allowWeight = trackWeight,
    equipment = equipment,
    muscleGroups = muscleGroups.toList(),
    difficulty = difficulty,
    isCompound = selectedType == ExerciseType.COMPOUND,
    videoUrl = videoUrl.trim().ifBlank { null },
    updatedAtEpochMs = 0L,
)

data class NewExerciseScreenState(
    val name: String = "",
    val description: String = "",
    val nameSr: String = "",
    val descriptionSr: String = "",
    val equipment: Equipment? = null,
    val selectedType: ExerciseType? = null,
    val trackWeight: Boolean = false,
    val muscleGroups: Set<MuscleGroup> = emptySet(),
    val difficulty: DifficultyLevel? = null,
    val videoUrl: String = "",
    val isSaving: Boolean = false,
    val error: NewExerciseError? = null,
)

enum class NewExerciseError {
    NameRequired,
    EquipmentRequired,
    TypeRequired,
    VideoUrlInvalid,
    Storage,
    Unknown,
}

sealed interface NewExerciseEvent {
    data object Saved : NewExerciseEvent
}
