package io.bbs.seva.vbbs004mobile.di

// di/NetworkModule.kt
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.data.remote.dto.ApiErrorBody
import io.bbs.seva.vbbs004mobile.data.remote.dto.AuthTokensDto
import io.bbs.seva.vbbs004mobile.data.repository.AuthRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.security.AuthTokens
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.session.SessionManager
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first

//val apiHost = runCatching {
//    Url(BuildConfig.BASE_URL).host
//}.getOrNull() ?: ""
val apiHost = Url(BuildConfig.BASE_URL).host
class UnauthorizedException(val serverMessage: String) : Exception(serverMessage)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"

    @Provides
    @Singleton
    fun provideHttpClient(
        @TokensDataStore authDataStore: DataStore<AuthTokens>,
        sessionManager: SessionManager,
    ): HttpClient {
        return HttpClient {
            expectSuccess = true
            HttpResponseValidator {
                handleResponseException { exception ->
                    if (exception is ClientRequestException &&
                        (exception.response.status == HttpStatusCode.Unauthorized ||
                                exception.response.status == HttpStatusCode.BadRequest)
                    ) {
                        // Read the raw JSON string safely from the response
                        val rawJson = exception.response.bodyAsText()

                        // Parse out the explicit backend error message
                        val serverMessage = try {
                            Json.decodeFromString<ApiErrorBody>(rawJson).error
                        } catch (e: Exception) {
                            "Unknown 40[01] error."
                        }

                        // Throw your structured exception
                        throw UnauthorizedException(serverMessage)
                    }
                }
            }
            install(Logging) {
                //level = LogLevel.BODY
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    // prettyPrint = true
                    isLenient = true
                    prettyPrint = BuildConfig.DEBUG
                })
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
//                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    // 1. Fetch token from Secure DataStore for standard outgoing requests
                    loadTokens {
                        val tokens = authDataStore.data.first()
                        Log.i(TAG, "provideHttpClient: token = $tokens")
                        if (tokens.accessToken != null && tokens.refreshToken != null) {
                            BearerTokens(tokens.accessToken!!, tokens.refreshToken)
                        } else {
                            null
                        }
                    }

                    // 2. This execution automatically triggers if a server responds with 401 Unauthorized
                    refreshTokens {
                        val currentTokens = authDataStore.data.first()
                        if (currentTokens.refreshToken == null) return@refreshTokens null
                        val refreshClient =
                            HttpClient {
                                install(ContentNegotiation) { json() }
                                install(Logging) {
                                    //level = LogLevel.BODY
                                    level = LogLevel.ALL
                                    logger = Logger.ANDROID
                                }
                            }
                        try {
                            // Create a clean, isolated client instance to avoid infinite 401 loops


                            val response =
                                refreshClient.post("${BuildConfig.BASE_URL}auth/refresh") {
                                    contentType(ContentType.Application.Json)
                                    setBody(mapOf("refresh_token" to currentTokens.refreshToken))
                                }
                            when (response.status) {
                                HttpStatusCode.OK -> {
                                    val newTokens =
                                        response.body<AuthTokensDto>()

                                    authDataStore.updateData {
                                        it.copy(
                                            accessToken = newTokens.accessToken,
                                            refreshToken = newTokens.refreshToken
                                        )
                                    }

                                    BearerTokens(
                                        accessToken = newTokens.accessToken.orEmpty(),
                                        refreshToken = newTokens.refreshToken.orEmpty()
                                    )
                                }

                                HttpStatusCode.Unauthorized -> {
                                    // Token refresh failed completely (e.g., Refresh Token expired or revoked)
                                    authDataStore.updateData { AuthTokens() } // Clear tokens
                                    sessionManager.emitLogout()
                                    null
                                }

                                else -> null

                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "provideHttpClient: !!!!!!!!!!!!!!!!!!!!!!!!!!!", e)
                            null
                        } finally {
                            refreshClient.close()
                        }
                    }

//                    // 3. Optional: Only run Bearer token insertion on specific API endpoints
                    sendWithoutRequest { request ->
                        request.url.host == apiHost && !request.url.pathSegments.contains(
                            "refresh"
                        )
                    }
                }
            }
        }
    }
}

