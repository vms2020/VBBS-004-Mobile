package io.bbs.seva.vbbs004mobile.data.repository
// data/repository/AuthRepositoryImpl.kt

import android.util.Log
import androidx.datastore.core.DataStore
import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import io.bbs.seva.vbbs004mobile.data.datastore.model.toDomain
import io.bbs.seva.vbbs004mobile.data.remote.dto.AuthResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.LoginRequest
import io.bbs.seva.vbbs004mobile.data.remote.dto.MeResponse
import io.bbs.seva.vbbs004mobile.data.security.AuthTokens
import io.bbs.seva.vbbs004mobile.di.ProfileDataStore
import io.bbs.seva.vbbs004mobile.di.TokensDataStore
import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    @TokensDataStore private val authDataStore: DataStore<AuthTokens>,
    @ProfileDataStore private val profileDataStore: DataStore<UserProfile>,
    val sessionManager: SessionManager,
) : AuthRepository {

    private val baseUrl = BuildConfig.BASE_URL

    override val isAuthenticated: Flow<Boolean> = authDataStore.data
        .map { tokens ->
            !tokens.accessToken.isNullOrBlank() && !tokens.refreshToken.isNullOrBlank()
        }

    override val userProfile: Flow<User?> = profileDataStore.data.map { profile ->
        // Return null if the user isn't logged in yet (no valid ID saved)
        if (profile.id.isNullOrBlank()) null else profile.toDomain()
    }

    override suspend fun login(email: String, password: String): Result<User> = runCatching {
        val response: AuthResponse = httpClient.post("${baseUrl}auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()

        // Save token to your datastore preference here if needed!
        authDataStore.updateData { currentTokens ->
            currentTokens.copy(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        }

        // Save profile data to the profile store
        profileDataStore.updateData {
            it.copy(
                fullName = response.user.userMetadata.fullName,
                avatarUrl = response.user.userMetadata.avatarUrl,
                age = response.user.userMetadata.age,
                email = response.user.email,
                id = response.user.id,
            )
        }

        User(
            response.user.id,
            response.user.email,
            response.user.userMetadata.fullName,
            response.user.userMetadata.avatarUrl,
            response.user.userMetadata.age
        )
    }

    override suspend fun signup(
        email: String,
        password: String,
        fullName: String?,
        age: Int?,
        avatarUrl: String?,
    ): Result<User> {
        TODO("Implement signup post request similarly")
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val response = httpClient.get("${baseUrl}auth/logout")
        Log.i(
            TAG,
            "logout: ${response.status.value} ${response.status.description} " +
                    "${response.status}"
        )
        authDataStore.updateData { AuthTokens() }
        profileDataStore.updateData { UserProfile() }
        sessionManager.emitLogout()
    }

    override suspend fun updateProfile(
        fullName: String?,
        age: Int?,
        avatarUrl: String?
    ): Result<User> {
        TODO()
    }

    override suspend fun me(): Result<User> = runCatching {
        val response: MeResponse = httpClient.get("${baseUrl}auth/me").body()
        profileDataStore.updateData { currentProfile ->
            currentProfile.copy(
                id = response.user.id,
                email = response.user.email,
                fullName = response.user.userMetadata.fullName,
                avatarUrl = response.user.userMetadata.avatarUrl,
                age = response.user.userMetadata.age
            )
        }
        User(
            response.user.id,
            response.user.email,
            response.user.userMetadata.fullName,
            response.user.userMetadata.avatarUrl,
            response.user.userMetadata.age
        )
    }
}
