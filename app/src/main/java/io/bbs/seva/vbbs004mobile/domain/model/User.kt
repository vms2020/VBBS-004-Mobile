package io.bbs.seva.vbbs004mobile.domain.model

// domain/model/User.kt
data class User(
    val id: String,
    val email: String,
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val age: Int? = null,
)
