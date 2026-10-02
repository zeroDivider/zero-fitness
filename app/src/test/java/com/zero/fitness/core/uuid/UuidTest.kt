package com.zero.fitness.core.uuid

import java.util.UUID
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UuidTest {

    @Test
    fun blankReturnsNull() {
        assertNull("".toUuidOrNull())
        assertNull("   ".toUuidOrNull())
    }

    @Test
    fun malformedReturnsNull() {
        assertNull("not-a-uuid".toUuidOrNull())
    }

    @Test
    fun validUuidParses() {
        val id = UUID.randomUUID()
        assertEquals(id, id.toString().toUuidOrNull())
    }
}
