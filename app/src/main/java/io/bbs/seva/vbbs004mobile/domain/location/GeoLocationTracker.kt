package io.bbs.seva.vbbs004mobile.domain.location

import android.location.Location

interface GeoLocationTracker {
    suspend fun getCurrentLocation(): android.location.Location?
    fun hasMovedSignificantly(oldLocation: Location?, newLocation: Location): Boolean

}