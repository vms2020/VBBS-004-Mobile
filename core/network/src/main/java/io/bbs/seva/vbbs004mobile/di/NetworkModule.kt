package io.bbs.seva.vbbs004mobile.di

// di/NetworkModule.kt
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// was: import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.core.network.BuildConfig
import io.bbs.seva.vbbs004mobile.core.network.proxy.DynamicProxySelector
import io.bbs.seva.vbbs004mobile.core.network.proxy.ProxyChangeInterceptor
import io.bbs.seva.vbbs004mobile.data.remote.dto.ApiErrorBody
import io.bbs.seva.vbbs004mobile.data.remote.dto.AuthTokensDto
// import io.bbs.seva.vbbs004mobile.data.repository.AuthRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.security.AuthTokens
import io.bbs.seva.vbbs004mobile.domain.model.ProxySettings
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.UserAgent
import io.ktor.client.request.header
import io.ktor.http.encodedPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

//val apiHost = runCatching {
//    Url(BuildConfig.BASE_URL).host
//}.getOrNull() ?: ""
//val apiHost = Url(BuildConfig.BASE_URL).host

class UnauthorizedException(val serverMessage: String) : Exception(serverMessage)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"

    private fun buildAndroidUserAgent(): String {
        val appName = "VBBS004 Mobile" // Or pull from R.string.app_name
        val appVersion = "v0.0.4-alpha" // Use BuildConfig.VERSION_NAME in a real app

        val osVersion = Build.VERSION.RELEASE
        val model = Build.MODEL
        val buildId = Build.ID

        return "$appName/$appVersion (Linux; U; Android $osVersion; $model Build/$buildId)"
    }

    @Provides
    @Singleton
    @BaseUrlState
    fun provideBaseUrlState(
        settingsRepository: AppSettingsRepository,
        @ApplicationScope appScope: CoroutineScope,
    ): StateFlow<String> =
        settingsRepository.appSettings
            .map { it.baseUrl.replace("\"", "").trim() }
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,   // MUST be Eagerly, not WhileSubscribed
                initialValue = BuildConfig.BASE_URL,
            )

    @Provides
    @Singleton
    fun provideProxyState(
        settingsRepository: AppSettingsRepository,
        @ApplicationScope appScope: CoroutineScope,
    ): StateFlow<ProxySettings> =
        settingsRepository.appSettings
            .map { it.proxy }
            .onEach { Log.e("ProxyState", "proxy → $it") }
            .stateIn(appScope, SharingStarted.Eagerly, ProxySettings(
                isEnabled = false,
                protocol = "SOCKS",
                host = "127.0.0.1",
                port = 0)
            )

    @Provides
    @Singleton
    fun provideHttpClient(
        @TokensDataStore authDataStore: DataStore<AuthTokens>,
        sessionManager: SessionManager,
        //settingsRepository: AppSettingsRepository,
        proxyState: StateFlow<ProxySettings>,
        @BaseUrlState baseUrlState: StateFlow<String>,
        @ApplicationScope appScope: CoroutineScope,
    ): HttpClient {

//        val okHttpClient = OkHttpClient.Builder()
//            .proxySelector(DynamicProxySelector(proxyState))
//            .build()
//
//        appScope.launch {
//            proxyState
//                .map { Triple(it.isEnabled, it.host, it.port) }
//                .distinctUntilChanged()
//                .drop(1)
//                .collect { okHttpClient.connectionPool.evictAll() }
//        }

        return HttpClient(OkHttp) {
            install(UserAgent) {
                // Example output: MyApp/1.4.2 (Linux; U; Android 14; Pixel 8 Pro Build/AP1A.240305.019)
                agent = buildAndroidUserAgent()
            }
            engine {

                config {
//                    proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 9150)))
                    //proxySelector(DynamicProxySelector(settingsRepository))
                    proxySelector(DynamicProxySelector(proxyState))
                    addInterceptor(ProxyChangeInterceptor(proxyState))
                }
            }
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
//                header
                //header("Connection", "close")
                //val base = baseUrlState.value
                val base = baseUrlState.value.trimEnd('/') + "/"
//                Log.e(TAG, "provideHttpClient:\n" +
//                        "base: $base\n" +
//                        "port: ${url.port}\n" +
//                        "user: ${url.user}\n" +
//                        "pathSegments: ${url.pathSegments}\n" +
//                        "encodedPath: ${url.encodedPath}\n" +
//                        "host: ${url.host}\n" +
//                        "base.isNotBlank() = ${base.isNotBlank()}\n" +
//                        "url.host.isBlack() = ${url.host.isBlank()}"
//                )
//                if (base.isNotBlank() && url.host.isBlank()) {
//                    val parsed = Url(base)
//                    url.protocol = parsed.protocol
//                    url.host = parsed.host
//                    url.port = parsed.port
//                    url.encodedPath = parsed.encodedPath.trimEnd('/') +
//                            url.encodedPath.let { if (it.startsWith("/")) it else "/$it" }
//                }
                if (base.isNotBlank()) {
                    url(base)
                }
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
                            HttpClient(OkHttp) {
                                install(UserAgent) {
                                    // Example output: MyApp/1.4.2 (Linux; U; Android 14; Pixel 8 Pro Build/AP1A.240305.019)
                                    agent = buildAndroidUserAgent()
                                }
                                engine {
                                    //preconfigured = okHttpClient
                                    config {
                                        proxySelector(DynamicProxySelector(proxyState))
                                        addInterceptor(ProxyChangeInterceptor(proxyState))
                                        //proxySelector(DynamicProxySelector(settingsRepository))
//                                      proxy(Proxy(Proxy.Type.SOCKS,InetSocketAddress("127.0.0.1", 9150)))
                                    }
                                }
                                install(ContentNegotiation) { json() }
                                install(Logging) {
                                    //level = LogLevel.BODY
                                    level = LogLevel.ALL
                                    logger = Logger.ANDROID
                                }

                            }
                        try {
                            // Create a clean, isolated client instance to avoid infinite 401 loops
                            //val refreshBase = baseUrlState.value.ifBlank { BuildConfig.BASE_URL }.trimEnd('/')
                            val refreshBase = baseUrlState.value.trimEnd('/')
                            val response =
                                refreshClient.post("$refreshBase/auth/refresh") {
                            //val response =
                            //    refreshClient.post("${BuildConfig.BASE_URL}auth/refresh") {
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
                        val path = request.url.pathSegments
                        val currentHost = runCatching { Url(baseUrlState.value).host }.getOrNull()
                        //request.url.host == apiHost
                        request.url.host == currentHost
                                && !path.contains("refresh")
                                && !path.contains("cbr")

//                        request.url.host == apiHost && !request.url.pathSegments.contains(
//                            "refresh"
//                        )
                    }
                }
            }
        }
    }
}


