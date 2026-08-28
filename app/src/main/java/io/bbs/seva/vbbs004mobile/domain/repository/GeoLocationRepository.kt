package io.bbs.seva.vbbs004mobile.domain.repository

import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation
import kotlinx.coroutines.flow.Flow

interface GeoLocationRepository {
    val savedGeoLocation: Flow<GeoLocation?>
    suspend fun saveLocation(geoLocation: GeoLocation)
}