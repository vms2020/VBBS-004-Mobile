package io.bbs.seva.vbbs004mobile.domain.constant

import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation

object LocationConstants {
    // A good default is a recognizable location, or just 0.0, 0.0
    val DEFAULT_LOCATION = GeoLocation(lat = 55.6107, lon = 37.7597)

    // You can also put other shared constants here
    const val DEFAULT_ZOOM = 13.0
    const val MIN_DISTANCE_CHANGE_FOR_UPDATE = 10.0 // meters
}