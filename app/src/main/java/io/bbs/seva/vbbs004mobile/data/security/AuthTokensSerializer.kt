package io.bbs.seva.vbbs004mobile.data.security

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class AuthTokens(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

object AuthTokensSerializer : Serializer<AuthTokens> {
    override val defaultValue: AuthTokens = AuthTokens()

    override suspend fun readFrom(input: InputStream): AuthTokens {
        return try {
            val decryptedBytes = CryptoManager.decrypt(input)
            Json.decodeFromString(AuthTokens.serializer(), decryptedBytes.decodeToString())
        } catch (e: Exception) {
            AuthTokens() // Return empty tokens if reading/decryption fails
        }
    }

    override suspend fun writeTo(t: AuthTokens, output: OutputStream) {
        val jsonString = Json.encodeToString(AuthTokens.serializer(), t)
        CryptoManager.encrypt(jsonString.encodeToByteArray(), output)
    }
}


