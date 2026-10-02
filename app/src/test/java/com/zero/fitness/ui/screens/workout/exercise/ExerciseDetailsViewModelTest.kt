@file:OptIn(ExperimentalCoroutinesApi::class)
package com.zero.fitness.ui.screens.workout.exercise

import androidx.lifecycle.SavedStateHandle
import com.zero.fitness.core.result.AppError
import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.model.SyncReport
import com.zero.fitness.domain.repository.ExerciseRepository
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ExerciseDetailsViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadSuccessMapsExerciseFields() = runTest {
        val id = UUID.randomUUID()
        val exercise = Exercise(
            id = id,
            name = "RDL",
            description = "Hinge",
            nameSr = "RMD",
            allowWeight = true,
            equipment = Equipment.BARBELL,
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS),
            difficulty = DifficultyLevel.INTERMEDIATE,
            isCompound = true,
            videoUrl = "https://youtube.com/watch?v=rdl",
            updatedAtEpochMs = 1L,
        )
        val viewModel = createViewModel(
            repository = FakeDetailsRepository(exercise),
            exerciseId = id.toString(),
        )

        val state = viewModel.viewState.value
        assertEquals(id.toString(), state.exerciseId)
        assertEquals("RDL", state.name)
        assertEquals("Hinge", state.description)
        assertEquals("RMD", state.nameSr)
        assertEquals(Equipment.BARBELL, state.equipment)
        assertEquals(ExerciseType.COMPOUND, state.selectedType)
        assertEquals(true, state.trackWeight)
        assertEquals(setOf(MuscleGroup.HAMSTRINGS), state.muscleGroups)
        assertEquals(DifficultyLevel.INTERMEDIATE, state.difficulty)
        assertEquals("https://youtube.com/watch?v=rdl", state.videoUrl)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun missingExerciseIsNotFound() = runTest {
        val viewModel = createViewModel(
            repository = FakeDetailsRepository(exercise = null),
            exerciseId = UUID.randomUUID().toString(),
        )

        assertEquals(ExerciseDetailsError.NotFound, viewModel.viewState.value.error)
        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun malformedIdIsNotFound() {
        val viewModel = createViewModel(
            repository = FakeDetailsRepository(exercise = null),
            exerciseId = "not-a-uuid",
        )

        assertEquals(ExerciseDetailsError.NotFound, viewModel.viewState.value.error)
        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun observeErrorMapsUnknown() = runTest {
        val viewModel = createViewModel(
            repository = FakeDetailsRepository(observeError = AppError.Unknown()),
            exerciseId = UUID.randomUUID().toString(),
        )

        assertEquals(ExerciseDetailsError.Unknown, viewModel.viewState.value.error)
        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun laterEmissionDoesNotShowLoading() = runTest {
        val id = UUID.randomUUID()
        val first = Exercise(id = id, name = "One", updatedAtEpochMs = 1L)
        val second = first.copy(name = "Two")
        val repository = FakeDetailsRepository(first)
        val viewModel = createViewModel(repository, id.toString())

        assertEquals("One", viewModel.viewState.value.name)
        assertFalse(viewModel.viewState.value.isLoading)

        repository.exercise.value = second

        assertEquals("Two", viewModel.viewState.value.name)
        assertFalse(viewModel.viewState.value.isLoading)
    }
}

private fun createViewModel(
    repository: FakeDetailsRepository,
    exerciseId: String,
) = ExerciseDetailsViewModel(
    SavedStateHandle(mapOf("exerciseId" to exerciseId)),
    repository,
)

private class FakeDetailsRepository(
    exercise: Exercise? = null,
    private val observeError: Throwable? = null,
) : ExerciseRepository {
    val exercise = MutableStateFlow(exercise)

    override fun observeAll(): Flow<List<Exercise>> = emptyFlow()

    override fun observePendingCount(): Flow<Int> = emptyFlow()

    override fun observeById(id: UUID): Flow<Exercise?> =
        if (observeError != null) {
            flow { throw observeError }
        } else {
            exercise
        }

    override suspend fun getById(id: UUID): Result<Exercise?> = Result.success(exercise.value)

    override suspend fun save(exercise: Exercise): Result<Unit> = Result.success(Unit)

    override suspend fun delete(id: UUID): Result<Unit> = Result.success(Unit)

    override suspend fun sync(): Result<SyncReport> =
        Result.success(SyncReport(pushed = 0, failed = 0, pulled = 0))
}
