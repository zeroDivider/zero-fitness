package com.zero.fitness.domain.model

data class SyncReport(
    val pushed: Int,
    val failed: Int,
    val pulled: Int,
    val skipped: Boolean = false,
)
