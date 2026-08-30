package io.bbs.seva.vbbs004mobile.domain.usecase

import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    // Define a data class to hold all the parameters neatly
    data class Params(
        val email: String,
        val password: String,
        val fullName: String?,
        val age: Int?,
        val avatarUrl: String?
    )

    suspend operator fun invoke(params: Params): Result<User> {
        // You could add validation logic here before calling the repository
        // e.g., if (params.email.isBlank()) return Result.failure(IllegalArgumentException("Email cannot be blank"))

        return authRepository.signup(
            email = params.email,
            password = params.password,
            fullName = params.fullName,
            age = params.age,
            avatarUrl = params.avatarUrl
        )
    }
}
