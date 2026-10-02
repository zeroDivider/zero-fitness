package com.zero.fitness.core.uuid

import java.util.UUID

fun String.toUuidOrNull(): UUID? {
    if (isBlank()) return null
    return try {
        UUID.fromString(this)
    } catch (_: IllegalArgumentException) {
        null
    }
}
