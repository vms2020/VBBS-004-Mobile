package io.bbs.seva.vbbs004mobile.domain.model

sealed interface AppError {
    data object Network : AppError
    data object Server : AppError
    data object Unknown : AppError
}
