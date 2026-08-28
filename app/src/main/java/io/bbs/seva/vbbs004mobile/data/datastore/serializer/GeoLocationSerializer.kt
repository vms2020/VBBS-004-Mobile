package io.bbs.seva.vbbs004mobile.data.datastore.serializer

import androidx.datastore.core.Serializer
import io.bbs.seva.vbbs004mobile.data.datastore.model.GeoLocationData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object GeoLocationSerializer : Serializer<GeoLocationData> {

    override val defaultValue: GeoLocationData = GeoLocationData()

    override suspend fun readFrom(input: InputStream): GeoLocationData {
        return try {
            Json.decodeFromString(
                deserializer = GeoLocationData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: GeoLocationData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = GeoLocationData.serializer(),
                value = t
            ).toByteArray()
        )
    }
}
