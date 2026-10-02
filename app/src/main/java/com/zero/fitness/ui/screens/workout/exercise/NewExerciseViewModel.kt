package com.zero.fitness.ui.screens.workout.exercise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zero.fitness.core.result.AppError
import com.zero.fitness.core.uuid.toUuidOrNull
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewExerciseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val routeId = savedStateHandle.get<String>("exerciseId").orEmpty()
    private val parsedRouteId = routeId.toUuidOrNull()
    private val isEdit = routeId.isNotBlank()
    private val exerciseId = parsedRouteId ?: UUID.randomUUID()
    private var loaded: Exercise? = null

    private val _viewState = MutableStateFlow(
        NewExerciseScreenState(
            isEdit = isEdit,
            isLoading = isEdit,
        ),
    )
    val viewState = _viewState.asStateFlow()

    private val _events = Channel<NewExerciseEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        if (isEdit) {
            if (parsedRouteId == null) {
                _viewState.update {
                    it.copy(isLoading = false, error = NewExerciseError.NotFound)
                }
            } else {
                viewModelScope.launch {
                    exerciseRepository.getById(parsedRouteId)
                        .onSuccess { exercise ->
                            if (exercise == null) {
                                _viewState.update {
                                    it.copy(isLoading = false, error = NewExerciseError.NotFound)
                                }
                            } else {
                                loaded = exercise
                                _viewState.update { exercise.toFormState() }
                            }
                        }
                        .onFailure { error ->
                            _viewState.update {
                                it.copy(
                                    isLoading = false,
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
    }

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
        if (_viewState.value.isSaving || _viewState.value.isLoading) return
        if (isEdit && loaded == null) {
            _viewState.update { it.copy(error = NewExerciseError.NotFound) }
            return
        }
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
        val exercise = state.toExercise(exerciseId, loaded)
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

private fun Exercise.toFormState() = NewExerciseScreenState(
    name = name,
    description = description.orEmpty(),
    nameSr = nameSr.orEmpty(),
    descriptionSr = descriptionSr.orEmpty(),
    equipment = equipment,
    selectedType = if (isCompound) ExerciseType.COMPOUND else ExerciseType.ISOLATION,
    trackWeight = allowWeight,
    muscleGroups = muscleGroups.toSet(),
    difficulty = difficulty,
    videoUrl = videoUrl.orEmpty(),
    isEdit = true,
    isLoading = false,
    error = null,
)

private fun NewExerciseScreenState.toExercise(id: UUID, loaded: Exercise?): Exercise {
    val formValues = Exercise(
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
        updatedAtEpochMs = loaded?.updatedAtEpochMs ?: 0L,
    )
    return loaded?.copy(
        name = formValues.name,
        description = formValues.description,
        nameSr = formValues.nameSr,
        descriptionSr = formValues.descriptionSr,
        allowWeight = formValues.allowWeight,
        equipment = formValues.equipment,
        muscleGroups = formValues.muscleGroups,
        difficulty = formValues.difficulty,
        isCompound = formValues.isCompound,
        videoUrl = formValues.videoUrl,
    ) ?: formValues
}

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
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: NewExerciseError? = null,
)

enum class NewExerciseError {
    NameRequired,
    EquipmentRequired,
    TypeRequired,
    VideoUrlInvalid,
    NotFound,
    Storage,
    Unknown,
}

sealed interface NewExerciseEvent {
    data object Saved : NewExerciseEvent
}
