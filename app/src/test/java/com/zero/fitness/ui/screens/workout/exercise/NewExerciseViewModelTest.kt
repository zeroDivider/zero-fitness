@file:OptIn(ExperimentalCoroutinesApi::class)
package com.zero.fitness.ui.screens.workout.exercise

import com.zero.fitness.domain.model.DifficultyLevel
import com.zero.fitness.domain.model.Equipment
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.ExerciseType
import com.zero.fitness.domain.model.MuscleGroup
import com.zero.fitness.domain.model.SyncReport
import com.zero.fitness.domain.repository.ExerciseRepository
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
import java.util.UUID

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
        val viewModel = NewExerciseViewModel(FakeExerciseRepository())

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
        val viewModel = NewExerciseViewModel(FakeExerciseRepository())

        viewModel.updateDifficulty(DifficultyLevel.BEGINNER)
        viewModel.updateDifficulty(DifficultyLevel.ELITE)

        assertEquals(DifficultyLevel.ELITE, viewModel.viewState.value.difficulty)
    }

    @Test
    fun saveRejectsInvalidVideoUrl() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = NewExerciseViewModel(repository)
        viewModel.fillRequiredFields()
        viewModel.updateVideoUrl("youtube.com/watch")

        viewModel.save()

        assertEquals(NewExerciseError.VideoUrlInvalid, viewModel.viewState.value.error)
        assertTrue(repository.saved.isEmpty())
    }

    @Test
    fun saveMapsBlankVideoUrlToNull() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = NewExerciseViewModel(repository)
        viewModel.fillRequiredFields()
        viewModel.updateVideoUrl("   ")

        viewModel.save()

        assertEquals(1, repository.saved.size)
        assertNull(repository.saved.single().videoUrl)
    }

    @Test
    fun saveIncludesMuscleGroupsDifficultyAndVideoUrl() = runTest {
        val repository = FakeExerciseRepository()
        val viewModel = NewExerciseViewModel(repository)
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
}

private fun NewExerciseViewModel.fillRequiredFields() {
    updateName("Squat")
    updateEquipment(Equipment.BARBELL)
    updateExerciseType(ExerciseType.COMPOUND)
}

private class FakeExerciseRepository : ExerciseRepository {
    val saved = mutableListOf<Exercise>()

    override fun observeAll(): Flow<List<Exercise>> = emptyFlow()

    override fun observePendingCount(): Flow<Int> = emptyFlow()

    override suspend fun getById(id: UUID): Result<Exercise?> = Result.success(null)

    override suspend fun save(exercise: Exercise): Result<Unit> {
        saved += exercise
        return Result.success(Unit)
    }

    override suspend fun delete(id: UUID): Result<Unit> = Result.success(Unit)

    override suspend fun sync(): Result<SyncReport> =
        Result.success(SyncReport(pushed = 0, failed = 0, pulled = 0))
}
