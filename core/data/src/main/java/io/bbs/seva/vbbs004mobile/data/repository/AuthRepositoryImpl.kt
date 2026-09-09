package io.bbs.seva.vbbs004mobile.data.repository
// data/repository/AuthRepositoryImpl.kt

import android.util.Log
import androidx.datastore.core.DataStore
//import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.core.network.BuildConfig
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import io.bbs.seva.vbbs004mobile.data.datastore.model.toDomain
import io.bbs.seva.vbbs004mobile.data.remote.dto.AuthResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.LoginRequest
import io.bbs.seva.vbbs004mobile.data.remote.dto.MeResponse
import io.bbs.seva.vbbs004mobile.data.remote.dto.changeprofile.ChangeProfileRequestDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.changeprofile.ChangeProfileResponseDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.pictures.AvaGetPicturesResponseDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.pictures.PicturesUploadResponseDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.signup.SignUpResponseDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.signup.SignupRequestDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.signup.toDomainUser
import io.bbs.seva.vbbs004mobile.data.security.AuthTokens
import io.bbs.seva.vbbs004mobile.di.ProfileDataStore
import io.bbs.seva.vbbs004mobile.di.TokensDataStore
import io.bbs.seva.vbbs004mobile.domain.model.AvaPic
import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant
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
//            contentType(ContentType.Application.Json)
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
    ): Result<User> = runCatching {
        val response: SignUpResponseDto = httpClient.post("${baseUrl}auth/signup") {
//            contentType(ContentType.Application.Json)
            setBody(
                SignupRequestDto(
                    email = email,
                    password = password,
                    fullName = fullName,
                    avatarUrl = avatarUrl,
                    age = age
                )
            )
        }.body()
        if (response.accessToken.isNullOrBlank()) {
            // Stop here, don't save anything, and throw an error to be caught by runCatching
            throw Exception("Registration successful! Please check your email to verify your account.")
        }
        authDataStore.updateData { currentTokens ->
            currentTokens.copy(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        }
        profileDataStore.updateData {
            it.copy(
                fullName = response.user?.userMetadata?.fullName,
                avatarUrl = response.user?.userMetadata?.avatarUrl,
                age = response.user?.userMetadata?.age,
                email = response.user?.email,
                id = response.user?.id,
            )
        }
        response.toDomainUser()
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        var r = authDataStore.data.first()
        Log.i(TAG, "logout: $r")
        var response = httpClient.post("${baseUrl}auth/logout") {
//            contentType(ContentType.Application.Json)
            setBody(
                mapOf("refresh_token" to r.refreshToken)
            )
        }
        Log.i(
            TAG,
            "logout: ${response.status.value} ${response.status.description} " +
                    "${response.status}"
        )
        if (response.status == HttpStatusCode.Unauthorized) {
            r = authDataStore.data.first()
            Log.i(TAG, "second logout: $r")
            response = httpClient.post("${baseUrl}auth/logout") {
                setBody(
                    mapOf("refresh_token" to r.refreshToken)
                )
            }
            Log.i(
                TAG,
                "second logout: ${response.status.value} ${response.status.description} " +
                        "${response.status}"
            )
        }

        authDataStore.updateData { AuthTokens() }
        profileDataStore.updateData { UserProfile() }
        sessionManager.emitLogout()
    }

    override suspend fun uploadPic(picArray: ByteArray): Result<String> = runCatching {
        val uploadResponse: io.ktor.client.statement.HttpResponse =
            httpClient.submitFormWithBinaryData(
                url = "${baseUrl}pictures/upload",
                formData = formData {
                    append("file", picArray, Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                        append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
                    })
                }
            )
        var finalAvatarUrl: String?
        if (uploadResponse.status == HttpStatusCode.Created) {
            // Parse response body containing metadata data block maps
            val responseBody: PicturesUploadResponseDto = uploadResponse.body()
            finalAvatarUrl = responseBody.data?.url
            if(finalAvatarUrl.isNullOrBlank()){
                throw Exception("pic avatar is null or blank")
            }
        } else {
            val errorBody: Map<String, String> = uploadResponse.body()
            throw Exception(errorBody["error"] ?: "Failed to upload avatar binary data.")
        }

        finalAvatarUrl
    }

    override suspend fun updateProfile(
        fullName: String?,
        age: Int?,
        avatarUrl: String?
    ): Result<User> = runCatching {

// region
//        // 1. If an image byte payload is present, upload it to the dedicated upload route
//        if (avatarUrl != null && avatarByteArray.isNotEmpty()) {
//            val uploadResponse: io.ktor.client.statement.HttpResponse =
//                httpClient.submitFormWithBinaryData(
//                    url = "${baseUrl}pictures/upload",
//                    formData = formData {
//                        append("file", avatarByteArray, Headers.build {
//                            append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
//                            append(HttpHeaders.ContentDisposition, "filename=\"avatar.jpg\"")
//                        })
//                    }
//                )
//
//            if (uploadResponse.status == HttpStatusCode.Created) {
//                // Parse response body containing metadata data block maps
//                val responseBody: PicturesUploadResponseDto = uploadResponse.body()
//                finalAvatarUrl = responseBody.data?.url
//            } else {
//                val errorBody: Map<String, String> = uploadResponse.body()
//                throw Exception(errorBody["error"] ?: "Failed to upload avatar binary data.")
//            }
//        }
// endregion

        val profile = profileDataStore.data.first()
        val updatedFullName = fullName?.takeIf { it.isNotBlank() } ?: profile.fullName
        val updatedAvatarUrl = avatarUrl?.takeIf { it.isNotBlank() } ?: profile.avatarUrl
        val updatedAge = age ?: profile.age
        if (updatedFullName == profile.fullName &&
            updatedAvatarUrl == profile.avatarUrl &&
            updatedAge == profile.age
        ) {
            throw IllegalArgumentException("No changes detected or valid fields provided to update.")
        }

        val requestBody = ChangeProfileRequestDto(
            fullName = fullName?.takeIf { it.isNotBlank() },
            avatarUrl = avatarUrl?.takeIf { it.isNotBlank() },
            age = age,
        )

        val response: ChangeProfileResponseDto = httpClient.patch("${baseUrl}auth/change-profile") {
            setBody(requestBody)
        }.body()

        // 2. Refresh local cache and trigger user metadata sync profiles if needed
        val updatedProfile = profileDataStore.updateData { currentProfile ->
            currentProfile.copy(
                fullName = response.user?.fullName,
                age = response.user?.age,
                avatarUrl = response.user?.avatarUrl
            )
        }

        updatedProfile.toDomain()
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

    // Inside your AuthRepositoryImpl class:
    override suspend fun fetchAvaPics(): Result<List<AvaPic>> = runCatching {
        // 1. Fetch the raw response body from the API endpoint
        val response: AvaGetPicturesResponseDto = httpClient.get("${baseUrl}pictures").body()

        // 2. Extract the base storage URL dynamically from the user's avatar_url.
        // Given: "https://my.vsevolod.dynv6.net:8443/storage/v1/object/public/user-uploads/..."
        // We isolate everything up to ".../user-uploads/" to reconstruction paths cleanly.
        //val avatarUrl = response.user?.avatarUrl
        val baseStorageUrl = response.user?.baseStoragePath
        //response.user?.id?.let { avatarUrl?.substring(0, avatarUrl.indexOf(it)) }

        // 3. Map the DTO items to clean Domain Models
        response.pictures?.map { picDto ->
            // Reconstruct absolute URL: Base path + storage folder layout path
            val fullUrl = "$baseStorageUrl${picDto?.storagePath}"

            // Parse the ISO 8601 date string ("2026-09-03T09:20:44.348391+00:00") into epoch ms
            //val epochMillis = Instant.parse(picDto?.createdAt).toEpochMilli()
            // Parse the ISO 8601 date string ("2026-09-05T13:53:07.403563+00:00") into epoch ms
            val epochMillis = picDto?.createdAt?.let { createdAtStr ->
                try {
                    // OffsetDateTime safely parses the numeric offset (+00:00)
                    java.time.OffsetDateTime.parse(createdAtStr).toInstant().toEpochMilli()
                } catch (e: Exception) {
                    e.printStackTrace()
                    0L // Fallback if formatting fails entirely
                }
            } ?: 0L

            AvaPic(
                url = fullUrl,
                createdAt = epochMillis
            )
        } ?: listOf()
    }

    override suspend fun deletePicture(storagePath: String): Result<Boolean> = runCatching {
        val r = httpClient.delete("${baseUrl}pictures") {
            setBody(mapOf("storage_path" to storagePath))
        }
        // TODO: check response body and refactor server code
        r.status == HttpStatusCode.OK
    }
}
