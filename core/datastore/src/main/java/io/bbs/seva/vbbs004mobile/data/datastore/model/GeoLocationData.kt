package io.bbs.seva.vbbs004mobile.data.datastore.model

// data/local/model/GeoLocationData.kt
import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation
import kotlinx.serialization.Serializable

@Serializable
data class GeoLocationData(
    val lat: Double? = null,
    val lon: Double? = null
)

fun GeoLocationData.toDomain(): GeoLocation? {
    this.lat?:return null
    this.lon?:return null
    return GeoLocation(
        lat = this.lat,
        lon = this.lon,
    )
}

