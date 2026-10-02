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
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {

    private val exerciseId = savedStateHandle.get<String>("exerciseId").orEmpty().toUuidOrNull()

    private val _viewState = MutableStateFlow(ExerciseDetailsScreenState())
    val viewState = _viewState.asStateFlow()

    init {
        if (exerciseId == null) {
            _viewState.update {
                it.copy(isLoading = false, error = ExerciseDetailsError.NotFound)
            }
        } else {
            viewModelScope.launch {
                exerciseRepository.observeById(exerciseId)
                    .catch { error ->
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                error = if (error is AppError.Storage) {
                                    ExerciseDetailsError.Storage
                                } else {
                                    ExerciseDetailsError.Unknown
                                },
                            )
                        }
                    }
                    .collect { exercise ->
                        _viewState.update { state ->
                            if (exercise == null) {
                                state.copy(
                                    isLoading = false,
                                    error = ExerciseDetailsError.NotFound,
                                )
                            } else {
                                exercise.toDetailsState()
                            }
                        }
                    }
            }
        }
    }
}

private fun Exercise.toDetailsState() = ExerciseDetailsScreenState(
    exerciseId = id.toString(),
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
    isLoading = false,
    error = null,
)

data class ExerciseDetailsScreenState(
    val exerciseId: String = "",
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
    val isLoading: Boolean = true,
    val error: ExerciseDetailsError? = null,
)

enum class ExerciseDetailsError {
    NotFound,
    Storage,
    Unknown,
}
