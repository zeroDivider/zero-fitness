@file:OptIn(ExperimentalCoroutinesApi::class)
package com.zero.fitness.ui.screens.workout.exercise

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewExerciseViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun toggleMuscleGroupAddsAndRemoves() {
        val viewModel = createViewModel()

        viewModel.toggleMuscleGroup(MuscleGroup.GLUTES)
        assertEquals(setOf(MuscleGroup.GLUTES), viewModel.viewState.value.muscleGroups)

        viewModel.toggleMuscleGroup(MuscleGroup.HAMSTRINGS)
        assertEquals(
            setOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
            viewModel.viewState.value.muscleGroups,
        )

        viewModel.toggleMuscleGroup(MuscleGroup.GLUTES)
        assertEquals(setOf(MuscleGroup.HAMSTRINGS), viewModel.viewState.value.muscleGroups)
    }

    @Test
    fun updateDifficultySelectsSingleLevel() {
        val viewModel = createViewModel()

        viewModel.updateDifficulty(DifficultyLevel.BEGINNER)
        viewModel.updateDifficulty(DifficultyLevel.ELITE)

        assertEquals(DifficultyLevel.ELITE, viewModel.viewState.value.difficulty)
    }

    @Test
    fun saveRejectsInvalidVideoUrl() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = createViewModel(repository)
        viewModel.fillRequiredFields()
        viewModel.updateVideoUrl("youtube.com/watch")

        viewModel.save()

        assertEquals(NewExerciseError.VideoUrlInvalid, viewModel.viewState.value.error)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun saveMapsBlankVideoUrlToNull() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = createViewModel(repository)
        viewModel.fillRequiredFields()
        viewModel.updateVideoUrl("   ")

        viewModel.save()

        assertEquals(1, repository.saved.size)
        assertNull(repository.saved.single().videoUrl)
    }

    @Test
    fun saveIncludesMuscleGroupsDifficultyAndVideoUrl() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = createViewModel(repository)
        viewModel.fillRequiredFields()
        viewModel.toggleMuscleGroup(MuscleGroup.QUADS)
        viewModel.toggleMuscleGroup(MuscleGroup.GLUTES)
        viewModel.updateDifficulty(DifficultyLevel.INTERMEDIATE)
        viewModel.updateVideoUrl("https://youtube.com/watch?v=example")

        viewModel.save()

        val saved = repository.saved.single()
        assertEquals(listOf(MuscleGroup.QUADS, MuscleGroup.GLUTES), saved.muscleGroups)
        assertEquals(DifficultyLevel.INTERMEDIATE, saved.difficulty)
        assertEquals("https://youtube.com/watch?v=example", saved.videoUrl)
    }

    @Test
    fun editSaveKeepsExistingIdMusclesAndVideoUrl() = runTest {
        val id = UUID.randomUUID()
        val existing = Exercise(
            id = id,
            name = "RDL",
            description = "Hinge",
            allowWeight = true,
            equipment = Equipment.BARBELL,
            muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES),
            difficulty = DifficultyLevel.INTERMEDIATE,
            isCompound = true,
            videoUrl = "https://youtube.com/watch?v=rdl",
            updatedAtEpochMs = 42L,
        )
        val repository = FakeExerciseRepository(existing = mapOf(id to existing))
        val viewModel = createViewModel(repository, exerciseId = id.toString())
        viewModel.updateName("Romanian deadlift")

        viewModel.save()

        val saved = repository.saved.single()
        assertEquals(id, saved.id)
        assertEquals("Romanian deadlift", saved.name)
        assertEquals(listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.GLUTES), saved.muscleGroups)
        assertEquals("https://youtube.com/watch?v=rdl", saved.videoUrl)
        assertEquals(42L, saved.updatedAtEpochMs)
    }

    @Test
    fun malformedEditIdIsNotFound() {
        val viewModel = createViewModel(exerciseId = "not-a-uuid")

        assertEquals(NewExerciseError.NotFound, viewModel.viewState.value.error)
        assertEquals(false, viewModel.viewState.value.isLoading)
    }
}

private fun createViewModel(
    repository: FakeExerciseRepository = FakeExerciseRepository(),
    exerciseId: String = "",
) = NewExerciseViewModel(
    SavedStateHandle(mapOf("exerciseId" to exerciseId)),
    repository,
)

private fun NewExerciseViewModel.fillRequiredFields() {
    updateName("Squat")
    updateEquipment(Equipment.BARBELL)
    updateExerciseType(ExerciseType.COMPOUND)
}

private class FakeExerciseRepository(
    private val existing: Map<UUID, Exercise> = emptyMap(),
) : ExerciseRepository {
    val saved = mutableListOf<Exercise>()

    override fun observeAll(): Flow<List<Exercise>> = emptyFlow()

    override fun observePendingCount(): Flow<Int> = emptyFlow()

    override fun observeById(id: UUID): Flow<Exercise?> = emptyFlow()

    override suspend fun getById(id: UUID): Result<Exercise?> =
        Result.success(existing[id])

    override suspend fun save(exercise: Exercise): Result<Unit> {
        saved += exercise
        return Result.success(Unit)
    }

    override suspend fun delete(id: UUID): Result<Unit> = Result.success(Unit)

    override suspend fun sync(): Result<SyncReport> =
        Result.success(SyncReport(pushed = 0, failed = 0, pulled = 0))
}
