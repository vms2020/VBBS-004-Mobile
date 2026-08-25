package io.bbs.seva.vbbs004mobile.data.remote.dto
// data/remote/dto/AuthDto.kt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val user: User,
    )


@Serializable
data class MeResponse(
    val user: User,
)

@Serializable
data class UserMetadata(
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val age: Int? = null,
)

@Serializable
data class AppMetadata(
    @SerialName("user_roles")
    val userRoles: List<String>? = null
)

@Serializable
data class User(
    val id: String,
    val email: String,
    @SerialName("user_metadata")
    val userMetadata: UserMetadata,
    @SerialName("app_metadata")
    val appMetadata: AppMetadata,
)