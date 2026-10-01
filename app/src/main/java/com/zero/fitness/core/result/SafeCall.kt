package com.zero.fitness.core.result

import android.database.sqlite.SQLiteException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException

inline fun <T> safeCall(block: () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e.toAppError())
    }

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is UnknownHostException,
    is SocketTimeoutException,
    is HttpRequestTimeoutException,
    is ConnectTimeoutException,
    is IOException,
    -> AppError.Network(this)
    is RestException ->
        if (statusCode == 401 || statusCode == 403) {
            AppError.Auth(this)
        } else {
            AppError.Unknown(this)
        }
    is SQLiteException -> AppError.Storage(this)
    else -> AppError.Unknown(this)
}
