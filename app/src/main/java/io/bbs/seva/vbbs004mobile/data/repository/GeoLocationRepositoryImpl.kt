package io.bbs.seva.vbbs004mobile.data.repository

import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation
import io.bbs.seva.vbbs004mobile.domain.repository.GeoLocationRepository
import androidx.datastore.core.DataStore
import io.bbs.seva.vbbs004mobile.data.datastore.model.GeoLocationData
import io.bbs.seva.vbbs004mobile.data.datastore.model.toDomain
import io.bbs.seva.vbbs004mobile.di.LocationDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GeoLocationRepositoryImpl @Inject constructor(
    @LocationDataStore private val locationDataStore: DataStore<GeoLocationData>
) : GeoLocationRepository {

    // 1. Read from DataStore and map to Domain Model
    override val savedGeoLocation: Flow<GeoLocation?> =
        locationDataStore.data.map { data ->
            data.toDomain()
        }

    // 2. Take Domain Model, map to DataStore model, and save
    override suspend fun saveLocation(geoLocation: GeoLocation) {
        locationDataStore.updateData { currentData ->
            currentData.copy(
                lat = geoLocation.lat,
                lon = geoLocation.lon
            )
        }
    }
}