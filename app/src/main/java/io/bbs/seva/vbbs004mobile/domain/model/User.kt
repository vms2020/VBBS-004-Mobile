package io.bbs.seva.vbbs004mobile.domain.model

// domain/model/User.kt
data class User(
    val id: String,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val age: Int?,
)
