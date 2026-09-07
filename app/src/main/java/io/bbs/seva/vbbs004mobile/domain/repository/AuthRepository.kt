package io.bbs.seva.vbbs004mobile.domain.repository
// domain/repository/AuthRepository.kt

import io.bbs.seva.vbbs004mobile.domain.model.AvaPic
import io.bbs.seva.vbbs004mobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuthenticated: Flow<Boolean>
    val userProfile: Flow<User?>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun signup(email: String, password: String, fullName: String?, age: Int?, avatarUrl: String?): Result<User>
    suspend fun logout(): Result<Unit>
    suspend fun updateProfile(fullName: String?, age: Int?, avatarUrl: String?): Result<User>
    suspend fun me(): Result<User>
    suspend fun fetchAvaPics(): Result<List<AvaPic>>
    suspend fun deletePicture(storagePath: String): Result<Boolean>
    suspend fun uploadPic(picArray: ByteArray): Result<String>
}
