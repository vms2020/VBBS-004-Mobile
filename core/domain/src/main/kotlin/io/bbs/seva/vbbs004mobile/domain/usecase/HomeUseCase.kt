package io.bbs.seva.vbbs004mobile.domain.usecase
// domain/usecase/HomeUseCase.kt

import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject



class HomeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    val userProfile: StateFlow<User?> = repository.userProfile

    suspend fun refreshProfile(): Result<User> {
        return repository.me()
    }
}