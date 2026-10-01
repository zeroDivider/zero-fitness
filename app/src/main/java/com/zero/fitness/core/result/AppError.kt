package com.zero.fitness.core.result

sealed class AppError(cause: Throwable? = null) : Exception(cause) {
    class Network(cause: Throwable? = null) : AppError(cause)
    class Auth(cause: Throwable? = null) : AppError(cause)
    class Storage(cause: Throwable? = null) : AppError(cause)
    class Unknown(cause: Throwable? = null) : AppError(cause)
}
