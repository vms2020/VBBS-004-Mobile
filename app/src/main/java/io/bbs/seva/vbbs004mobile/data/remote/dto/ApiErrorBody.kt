package io.bbs.seva.vbbs004mobile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorBody(
    val error: String
)
