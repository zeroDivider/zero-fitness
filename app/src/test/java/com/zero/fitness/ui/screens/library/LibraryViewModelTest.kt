@file:OptIn(ExperimentalCoroutinesApi::class)
package com.zero.fitness.ui.screens.library

import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.model.SyncReport
import com.zero.fitness.domain.repository.ExerciseRepository
import com.zero.fitness.domain.sync.SyncPolicy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.UUID

class LibraryViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun stateReflectsExercisesPendingCountAndSyncPolicy() {
        runTest {
            val squat = exercise("Squat")
            val repository = FakeExerciseRepository(
                exercises = listOf(squat),
                pendingCount = 2,
            )
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy(canSync = true))

            assertEquals(LibraryScreenState(), viewModel.state.value)
            collectState(viewModel)

            assertEquals(
                LibraryScreenState(
                    exercises = listOf(squat),
                    pendingCount = 2,
                    syncEnabled = true,
                ),
                viewModel.state.value,
            )
        }
    }

    @Test
    fun stateUpdatesWhenExercisesPendingCountOrSyncPolicyChange() {
        runTest {
            val repository = FakeExerciseRepository()
            val policy = FakeSyncPolicy(canSync = false)
            val viewModel = LibraryViewModel(repository, policy)
            collectState(viewModel)

            val squat = exercise("Squat")
            repository.exercises.value = listOf(squat)
            repository.pendingCount.value = 4
            policy.canSync.value = true

            assertEquals(
                LibraryScreenState(
                    exercises = listOf(squat),
                    pendingCount = 4,
                    syncEnabled = true,
                ),
                viewModel.state.value,
            )
        }
    }

    @Test
    fun syncFinishesIdleWhenRepositorySucceeds() {
        runTest {
            val repository = FakeExerciseRepository()
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy(canSync = true))
            collectState(viewModel)

            viewModel.sync()

            assertEquals(1, repository.syncCalls)
            assertEquals(false, viewModel.state.value.isSyncing)
            assertEquals(false, viewModel.state.value.syncFailed)
        }
    }

    @Test
    fun syncSetsFailedFlagWhenRepositoryFails() {
        runTest {
            val repository = FakeExerciseRepository().apply {
                syncResult = Result.failure(IOException("offline"))
            }
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy(canSync = true))
            collectState(viewModel)

            viewModel.sync()

            assertEquals(true, viewModel.state.value.syncFailed)
            assertEquals(false, viewModel.state.value.isSyncing)
        }
    }

    @Test
    fun syncDoesNotStartWhileAlreadySyncing() {
        runTest {
            val gate = CompletableDeferred<Result<SyncReport>>()
            val repository = FakeExerciseRepository().apply { syncGate = gate }
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy(canSync = true))
            collectState(viewModel)

            viewModel.sync()
            viewModel.sync()

            assertEquals(1, repository.syncCalls)
            assertEquals(true, viewModel.state.value.isSyncing)
            assertEquals(false, viewModel.state.value.syncFailed)

            gate.complete(Result.success(SyncReport(pushed = 1, failed = 0, pulled = 0)))

            assertEquals(false, viewModel.state.value.isSyncing)
            assertEquals(false, viewModel.state.value.syncFailed)
        }
    }

    @Test
    fun syncClearsFailedFlagWhenANewSyncStarts() {
        runTest {
            val repository = FakeExerciseRepository().apply {
                syncResult = Result.failure(IOException("offline"))
            }
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy(canSync = true))
            collectState(viewModel)

            viewModel.sync()
            assertEquals(true, viewModel.state.value.syncFailed)

            val gate = CompletableDeferred<Result<SyncReport>>()
            repository.syncGate = gate
            viewModel.sync()

            assertEquals(false, viewModel.state.value.syncFailed)
            assertEquals(true, viewModel.state.value.isSyncing)

            gate.complete(Result.failure(IOException("still offline")))

            assertEquals(true, viewModel.state.value.syncFailed)
            assertEquals(false, viewModel.state.value.isSyncing)
        }
    }

    @Test
    fun deleteForwardsExerciseId() {
        runTest {
            val repository = FakeExerciseRepository()
            val viewModel = LibraryViewModel(repository, FakeSyncPolicy())
            val id = UUID.randomUUID()

            viewModel.delete(id)

            assertEquals(listOf(id), repository.deletedIds)
        }
    }


    fun TestScope.collectState(viewModel: LibraryViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect {}
        }
    }

    fun exercise(name: String) = Exercise(
        id = UUID.randomUUID(),
        name = name,
        updatedAtEpochMs = 0L,
    )
}

class FakeSyncPolicy(canSync: Boolean = false) : SyncPolicy {
    val canSync = MutableStateFlow(canSync)

    override fun observeCanSync(): Flow<Boolean> = this.canSync

    override suspend fun canSync(): Boolean = this.canSync.value
}

class FakeExerciseRepository(
    exercises: List<Exercise> = emptyList(),
    pendingCount: Int = 0,
) : ExerciseRepository {
    val exercises = MutableStateFlow(exercises)
    val pendingCount = MutableStateFlow(pendingCount)
    var syncResult: Result<SyncReport> =
        Result.success(SyncReport(pushed = 0, failed = 0, pulled = 0))
    var syncGate: CompletableDeferred<Result<SyncReport>>? = null
    var syncCalls: Int = 0
    val deletedIds = mutableListOf<UUID>()

    override fun observeAll(): Flow<List<Exercise>> = exercises

    override fun observePendingCount(): Flow<Int> = pendingCount

    override suspend fun getById(id: UUID): Result<Exercise?> = Result.success(null)

    override suspend fun save(exercise: Exercise): Result<Unit> = Result.success(Unit)

    override suspend fun delete(id: UUID): Result<Unit> {
        deletedIds += id
        return Result.success(Unit)
    }

    override suspend fun sync(): Result<SyncReport> {
        syncCalls += 1
        return syncGate?.await() ?: syncResult
    }
}
