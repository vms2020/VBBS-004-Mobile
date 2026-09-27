package io.bbs.seva.vbbs004mobile.data.remote.mapper

import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import java.io.IOException

/**
 * Transport-failure classification at the boundary.
 * Ktor maps raw java.net socket failures into its own types before we see them —
 * hence the io.ktor imports (NOT java.net — that was the 'always false' bug).
 */
fun Throwable.toAppError(): AppError = when {
    this is SocketTimeoutException ||
            this is ConnectTimeoutException ||
            this is IOException -> AppError.Network
    else -> AppError.Unknown
}
