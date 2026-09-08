package io.bbs.seva.vbbs004mobile.domain.location

import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation

interface GeoLocationTracker {
    suspend fun getCurrentLocation(): GeoLocation?
    fun hasMovedSignificantly(oldLocation: GeoLocation?, newLocation: GeoLocation): Boolean

}