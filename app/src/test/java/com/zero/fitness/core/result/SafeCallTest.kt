package com.zero.fitness.core.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SafeCallTest {

    @Test(expected = CancellationException::class)
    fun safeCallRethrowsCancellation() {
        runBlocking {
            safeCall { throw CancellationException("stop") }
        }
    }
}
