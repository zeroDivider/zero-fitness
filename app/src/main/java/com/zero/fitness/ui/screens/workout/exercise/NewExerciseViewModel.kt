package com.zero.fitness.ui.screens.workout.exercise

import androidx.lifecycle.ViewModel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class NewExerciseViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(NewExerciseScreenState())
    val viewState = _viewState.asStateFlow()

    fun updateName(newName: String) {
        _viewState.update { it.copy(name = newName) }
    }

    fun updateDescription(newDescription: String) {
        _viewState.update { it.copy(description = newDescription) }
    }

    fun updateExerciseType(type: ExerciseType) {
        _viewState.update { it.copy(selectedType = type) }
    }


}

data class NewExerciseScreenState(
    val name: String = "",
    val description: String = "",
    val equipment: Equipment? = null,
    val selectedType: ExerciseType? = null,
    val trackWeight: Boolean = false,
    val isLoading: Boolean = false,
    val showError: Boolean? = null,
)

sealed class ViewState {
    data class Loading(val isLoading: Boolean = false) : ViewState()
    data class Error(val error: String) : ViewState()
    data class State(
        val name: String = "",
        val description: String = "",
        val equipment: Equipment? = null,
        val type: ExerciseType? = null,
        val trackWeight: Boolean = false,
        //val isLoading: Boolean = false,
        //val showError: Boolean? = null,
    ) : ViewState()
}
