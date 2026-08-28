package io.bbs.seva.vbbs004mobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    val lat: Double,
    val lon: Double
)
