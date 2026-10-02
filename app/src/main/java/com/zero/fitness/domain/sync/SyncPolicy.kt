package com.zero.fitness.domain.sync

import kotlinx.coroutines.flow.Flow

interface SyncPolicy {
    fun observeCanSync(): Flow<Boolean>
    suspend fun canSync(): Boolean
}
