package com.zero.fitness.data.repository

import com.zero.fitness.data.local.db.dao.ExerciseDao
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import com.zero.fitness.data.remote.ExerciseRemoteDataSource
import com.zero.fitness.data.remote.dto.ExerciseDto
import com.zero.fitness.domain.model.Exercise
import com.zero.fitness.domain.sync.SyncPolicy
import java.io.IOException
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseRepositoryImplTest {

    @Test
    fun saveDoesNotCallRemoteWhenUpsertWouldFail() = runBlocking {
        val dao = FakeExerciseDao()
        val remote = FakeRemote(onUpsert = { throw IOException("offline") })
        val repository = repository(dao, remote)

        val id = UUID.randomUUID()
        val result = repository.save(exercise(id, "Squat"))

        assertTrue(result.isSuccess)
        assertEquals(0, remote.upsertCalls)
        val row = dao.snapshot().single()
        assertEquals(id, row.id)
        assertEquals("Squat", row.name)
        assertTrue(row.syncPending)
    }

    @Test
    fun saveSameIdTwiceKeepsOneRow() = runBlocking {
        val dao = FakeExerciseDao()
        val repository = repository(dao, FakeRemote())
        val id = UUID.randomUUID()

        repository.save(exercise(id, "Squat"))
        repository.save(exercise(id, "Front squat"))

        val rows = dao.snapshot()
        assertEquals(1, rows.size)
        assertEquals("Front squat", rows.single().name)
    }

    @Test
    fun syncKeepsRowPendingWhenUpsertFails() = runBlocking {
        val dao = FakeExerciseDao()
        val id = UUID.randomUUID()
        dao.upsert(entity(id, syncPending = true))
        val remote = FakeRemote(onUpsert = { throw IOException("offline") })
        val repository = repository(dao, remote)

        val report = repository.sync().getOrThrow()

        assertEquals(1, report.failed)
        assertEquals(0, report.pushed)
        assertTrue(dao.getRaw(id)!!.syncPending)
    }

    @Test
    fun pullDoesNotOverwritePendingRow() = runBlocking {
        val dao = FakeExerciseDao()
        val id = UUID.randomUUID()
        dao.upsert(entity(id, name = "Local", syncPending = true, updatedAtEpochMs = 10L))
        val remote = FakeRemote(
            onUpsert = { throw IOException("offline") },
            fetched = listOf(dto(id, name = "Remote")),
        )
        val repository = repository(dao, remote)

        repository.sync().getOrThrow()

        val row = dao.getRaw(id)!!
        assertEquals("Local", row.name)
        assertTrue(row.syncPending)
        assertEquals(10L, row.updatedAtEpochMs)
    }

    @Test
    fun rowEditedDuringPushStaysPending() = runBlocking {
        val dao = FakeExerciseDao()
        val id = UUID.randomUUID()
        dao.upsert(entity(id, updatedAtEpochMs = 10L, syncPending = true))
        val remote = FakeRemote(
            onUpsert = {
                val current = dao.getRaw(id)!!
                dao.upsert(current.copy(updatedAtEpochMs = 99L))
            },
        )
        val repository = repository(dao, remote)

        repository.sync().getOrThrow()

        val row = dao.getRaw(id)!!
        assertTrue(row.syncPending)
        assertEquals(99L, row.updatedAtEpochMs)
    }

    @Test
    fun syncIsSkippedWhenPolicyDisallowsIt() = runBlocking {
        val remote = FakeRemote()
        val repository = repository(
            dao = FakeExerciseDao(),
            remote = remote,
            policy = FakeSyncPolicy(allowed = false),
        )

        val report = repository.sync().getOrThrow()

        assertTrue(report.skipped)
        assertEquals(0, remote.fetchCalls)
        assertEquals(0, remote.upsertCalls)
    }

    private fun repository(
        dao: FakeExerciseDao,
        remote: FakeRemote,
        policy: SyncPolicy = FakeSyncPolicy(allowed = true),
    ) = ExerciseRepositoryImpl(dao, remote, policy)

    private fun exercise(id: UUID, name: String) = Exercise(
        id = id,
        name = name,
        updatedAtEpochMs = 0L,
    )

    private fun entity(
        id: UUID,
        name: String = "Squat",
        updatedAtEpochMs: Long = 10L,
        syncPending: Boolean = true,
    ) = ExerciseEntity(
        id = id,
        name = name,
        updatedAtEpochMs = updatedAtEpochMs,
        syncPending = syncPending,
    )

    private fun dto(id: UUID, name: String) = ExerciseDto(
        id = id.toString(),
        name = name,
        createdAtEpochMs = 1L,
        updatedAtEpochMs = 2L,
    )
}

private class FakeSyncPolicy(private val allowed: Boolean) : SyncPolicy {
    override fun observeCanSync(): Flow<Boolean> = flowOf(allowed)

    override suspend fun canSync(): Boolean = allowed
}

private class FakeRemote(
    private val onUpsert: suspend (ExerciseDto) -> Unit = {},
    private val fetched: List<ExerciseDto> = emptyList(),
) : ExerciseRemoteDataSource {
    var upsertCalls: Int = 0
    var fetchCalls: Int = 0

    override suspend fun fetchAll(): List<ExerciseDto> {
        fetchCalls += 1
        return fetched
    }

    override suspend fun upsert(dto: ExerciseDto) {
        upsertCalls += 1
        onUpsert(dto)
    }

    override suspend fun delete(id: String) = Unit
}

private class FakeExerciseDao : ExerciseDao {
    private val rows = linkedMapOf<UUID, ExerciseEntity>()

    fun snapshot(): List<ExerciseEntity> = rows.values.toList()

    override fun observeAll(): Flow<List<ExerciseEntity>> = flow {
        emit(rows.values.filter { !it.deletedLocally }.sortedBy { it.name.lowercase() })
    }

    override fun observePendingCount(): Flow<Int> = flow {
        emit(rows.values.count { it.syncPending })
    }

    override suspend fun getById(id: UUID): ExerciseEntity? =
        rows[id]?.takeUnless { it.deletedLocally }

    override suspend fun getRaw(id: UUID): ExerciseEntity? = rows[id]

    override suspend fun upsert(entity: ExerciseEntity) {
        rows[entity.id] = entity
    }

    override suspend fun upsertAll(entities: List<ExerciseEntity>) {
        entities.forEach { upsert(it) }
    }

    override suspend fun markDeleted(id: UUID, pending: Boolean, updatedAtEpochMs: Long) {
        val current = rows[id] ?: return
        rows[id] = current.copy(
            deletedLocally = true,
            syncPending = pending,
            updatedAtEpochMs = updatedAtEpochMs,
        )
    }

    override suspend fun getPending(): List<ExerciseEntity> =
        rows.values.filter { it.syncPending }

    override suspend fun markSynced(id: UUID, pushedUpdatedAtEpochMs: Long) {
        val current = rows[id] ?: return
        if (current.updatedAtEpochMs == pushedUpdatedAtEpochMs) {
            rows[id] = current.copy(syncPending = false)
        }
    }

    override suspend fun hardDelete(id: UUID) {
        rows.remove(id)
    }

    override suspend fun getSyncedIds(): List<UUID> =
        rows.values.filter { !it.syncPending }.map { it.id }

    override suspend fun deleteSyncedByIds(ids: List<UUID>) {
        ids.forEach { id ->
            if (rows[id]?.syncPending == false) {
                rows.remove(id)
            }
        }
    }
}
