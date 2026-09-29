package io.bbs.seva.vbbs004mobile.data.datastore.serializer

import androidx.datastore.core.Serializer
import io.bbs.seva.vbbs004mobile.data.datastore.model.AppSettingsData
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object AppSettingsSerializer : Serializer<AppSettingsData> {
    override val defaultValue: AppSettingsData
        get() = AppSettingsData()

    override suspend fun readFrom(input: InputStream): AppSettingsData {
        return try {
            Json.decodeFromString(
                deserializer = AppSettingsData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppSettingsData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettingsData.serializer(),
                value = t
            ).toByteArray()
        )
    }
}