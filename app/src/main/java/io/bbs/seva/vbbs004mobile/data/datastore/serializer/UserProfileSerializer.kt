package io.bbs.seva.vbbs004mobile.data.datastore.serializer

import androidx.datastore.core.Serializer
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserProfileSerializer : Serializer<UserProfile> {

    override val defaultValue: UserProfile = UserProfile()

    override suspend fun readFrom(input: InputStream): UserProfile {
        return try {
            Json.decodeFromString(
                deserializer = UserProfile.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue // Fallback safely if file is corrupted
        }
    }

    override suspend fun writeTo(t: UserProfile, output: OutputStream) {
        //return withContext(Dispatchers.IO) {
        output.write(
            Json.encodeToString(
                serializer = UserProfile.serializer(),
                value = t
            ).toByteArray()
        )
        //}
    }
}
