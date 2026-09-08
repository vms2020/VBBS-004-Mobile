package io.bbs.seva.vbbs004mobile.data.location

import android.app.Application
import io.bbs.seva.vbbs004mobile.domain.location.GeoLocationTracker
import javax.inject.Inject
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.core.content.ContextCompat
import io.bbs.seva.vbbs004mobile.domain.model.GeoLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class DefaultGeoLocationTracker @Inject constructor(
    private val application: Application,
) : GeoLocationTracker {
    override suspend fun getCurrentLocation(): GeoLocation? {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            application, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocationPermission) {
            return null
        }

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val provider = when {
            isGpsEnabled -> LocationManager.GPS_PROVIDER
            isNetworkEnabled -> LocationManager.NETWORK_PROVIDER
            else -> return null
        }

        return suspendCancellableCoroutine { continuation ->
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    if (continuation.isActive) {
                        continuation.resume(GeoLocation(location.latitude, location.longitude))
                    }
                }

                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            try {
                // Check if device runs Android 12 (API 31) or newer
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // Modern non-deprecated approach
                    val locationRequest =
                        LocationRequest.Builder(0) // 0 ms interval for immediate update
                            .setMaxUpdates(1) // Forces the stream engine to close automatically after 1 update
                            .setQuality(LocationRequest.QUALITY_HIGH_ACCURACY)
                            .build()

                    locationManager.requestLocationUpdates(
                        provider,
                        locationRequest,
                        application.mainExecutor,
                        locationListener
                    )
                } else {
                    // Clean backward compatibility fallback route for older OS versions
                    @Suppress("DEPRECATION")
                    locationManager.requestLocationUpdates(
                        provider,
                        1000L, // 1 second minimum time interval
                        0f,    // 0 meters minimum distance interval
                        locationListener,
                        application.mainLooper
                    )
                }
            } catch (e: SecurityException) {
                if (continuation.isActive) continuation.resume(null)
            }

            continuation.invokeOnCancellation {
                locationManager.removeUpdates(locationListener)
            }
        }
    }

    override fun hasMovedSignificantly(
        oldLocation: GeoLocation?,
        newLocation: GeoLocation
    ): Boolean {
        if (oldLocation == null) return true
        val results = FloatArray(1)

        Location.distanceBetween(
            oldLocation.lat, oldLocation.lon,
            newLocation.lat, newLocation.lon,
            results
        )
        return results[0] > 10000f
    }

}