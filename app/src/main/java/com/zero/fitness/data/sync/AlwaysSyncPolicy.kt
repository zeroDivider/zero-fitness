package com.zero.fitness.data.sync

import com.zero.fitness.domain.sync.SyncPolicy
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** To be replaced by a premium entitlement check later. */
class AlwaysSyncPolicy @Inject constructor() : SyncPolicy {
    override fun observeCanSync(): Flow<Boolean> = flowOf(true)

    override suspend fun canSync(): Boolean = true
}
