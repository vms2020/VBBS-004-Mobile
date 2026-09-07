package io.bbs.seva.vbbs004mobile.data.remote.dto.changeprofile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileRequestDto(
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val age: Int? = null
)
