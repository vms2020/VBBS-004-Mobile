package io.bbs.seva.vbbs004mobile.data.datastore.model
// data/datastore/model/UserProfile.kt

import io.bbs.seva.vbbs004mobile.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val avatarUrl: String? = null,
    val age: Int? = null
)

// The Mapper: Converts your data layer model to your domain layer model
fun UserProfile.toDomain(): User {
    return User(
        id = this.id ?: "",
        email = this.email ?: "",
        fullName = this.fullName,
        avatarUrl = this.avatarUrl,
        age = this.age
    )
}
