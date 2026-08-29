package io.bbs.seva.vbbs004mobile.presentation.screens.osm

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.constant.LocationConstants
import io.bbs.seva.vbbs004mobile.domain.location.GeoLocationTracker
import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation
import io.bbs.seva.vbbs004mobile.domain.repository.GeoLocationRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OsmPickerViewModel @Inject constructor(
    private val locationTracker: GeoLocationTracker,
    private val locationStorage: GeoLocationRepository,
) : ViewModel() {
    private var _activeLocationAnchor: Location? = null
    val activeLocationAnchor: Location?
        get() = _activeLocationAnchor

    fun getFreshGpsLocation(onLocationFetched: (lat: Double, lon: Double) -> Unit) {
        viewModelScope.launch {
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                onLocationFetched(location.latitude, location.longitude)
            } else {
                onLocationFetched(
                    LocationConstants.DEFAULT_LOCATION.lat, //55.75,
                    LocationConstants.DEFAULT_LOCATION.lon, //37.61,
                )
            }
        }
    }

    fun saveLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            locationStorage.saveLocation(GeoLocation(lat, lon))
        }
    }

}