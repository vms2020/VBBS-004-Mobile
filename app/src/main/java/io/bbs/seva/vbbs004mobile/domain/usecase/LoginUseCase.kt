package io.bbs.seva.vbbs004mobile.domain.usecase
// domain/usecase/LoginUseCase.kt

import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import javax.inject.Inject



class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Invalid credentials syntax"))
        }
        return repository.login(email, password)
    }
}