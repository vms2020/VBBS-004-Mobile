package io.bbs.seva.vbbs004mobile.presentation.screens.osm

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.bbs.seva.vbbs004mobile.domain.constant.LocationConstants
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

private const val TAG = "OsmPickerScreen"

@Composable
fun OsmPickerScreen(
    initialLatitude: Double,
    initialLongitude: Double,
    onForceGpsRequest: ((lat: Double, lon: Double) -> Unit) -> Unit,
    onLocationSelected: (lat: Double, lon: Double) -> Unit,
    onCancelSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeMarker by remember { mutableStateOf<Marker?>(null) }
    var selectedCoordinates by remember { mutableStateOf<GeoPoint?>(null) }
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    Log.i(TAG, "OsmPickerScreen: initialLatitude $initialLatitude initialLongitude $initialLongitude")

    // Parent Stack Container handles z-index layering automatically
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        // -------------------------------------------------------------
        // LAYER 1 (Bottom): The Map Canvas expands underneath everything
        // -------------------------------------------------------------
        Column(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                factory = { context ->
                    Configuration.getInstance().userAgentValue = context.packageName
                    MapView(context).apply {
                        setMultiTouchControls(true)
                        isTilesScaledToDpi = true
                        controller.setZoom(LocationConstants.DEFAULT_ZOOM)// 13.0)

                        val startPoint = GeoPoint(initialLatitude, initialLongitude)
                        controller.setCenter(startPoint)
                        selectedCoordinates = startPoint
                        mapViewInstance = this

                        activeMarker = Marker(this).apply {
                            position = startPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Выбранный район"
                        }
                        overlays.add(activeMarker)
//                        activeMarker?.showInfoWindow()

                        val mapEventsReceiver = object : MapEventsReceiver {
                            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                                selectedCoordinates = p
                                if (activeMarker != null) overlays.remove(activeMarker)

                                activeMarker = Marker(this@apply).apply {
                                    position = p
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = "Выбранная точка\n" +
                                            "lat: ${selectedCoordinates?.latitude}\n" +
                                            "lon: ${selectedCoordinates?.longitude}"
                                }
                                overlays.add(activeMarker)
                                invalidate()
                                return true
                            }
                            override fun longPressHelper(p: GeoPoint): Boolean = false
                        }
                        overlays.add(MapEventsOverlay(mapEventsReceiver))
                    }
                },
                update = { mapViewInstance = it }
            )

            // Bottom Confirmation action bar shelf container bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        selectedCoordinates?.let {
                            onLocationSelected(it.latitude, it.longitude)
                        }
                    },
                    enabled = selectedCoordinates != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Подтвердить выбор места")
                }
            }
        }

        // -------------------------------------------------------------
        // LAYER 2 (Top): Floating Header Toolbar Row (Bypasses Map Drawing)
        // -------------------------------------------------------------
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .statusBarsPadding() // Pushes it perfectly beneath the phone's system clock/status items
//                .padding(8.dp)
//                .align(Alignment.TopCenter),
//            shape = RoundedCornerShape(12.dp), // Makes it a beautiful floating card element
//            color = MaterialTheme.colorScheme.surface,
//            tonalElevation = 4.dp, // Applies a slight dark tint for contrast depth
//            shadowElevation = 6.dp // Guarantees the OS draws a drop shadow *above* the hardware map view canvas
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp, vertical = 6.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                FilledIconButton(onClick = onCancelSelected) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Отмена"
//                    )
//                }
//                Text(
//                    text = "Выбор местоположения",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(start = 12.dp)
//                )
//            }
//        }

        // -------------------------------------------------------------
        // LAYER 3 (Top): Floating Target GPS Recenter Circle Button
        // -------------------------------------------------------------
        SmallFloatingActionButton(
            onClick = {
                onForceGpsRequest { freshLat, freshLon ->
                    val freshPoint = GeoPoint(freshLat, freshLon)
                    selectedCoordinates = freshPoint

                    mapViewInstance?.let { mapView ->
                        mapView.controller.animateTo(freshPoint)
                        mapView.controller.setZoom(LocationConstants.DEFAULT_ZOOM) //14.0)

                        if (activeMarker != null) mapView.overlays.remove(activeMarker)
                        activeMarker = Marker(mapView).apply {
                            position = freshPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = "Ваше текущее местоположение\nlat: ${freshPoint.latitude}\nlon: ${freshPoint.longitude}"
                        }
                        mapView.overlays?.add(activeMarker) // Handle internal reference safety checks smoothly
                        mapView.invalidate()
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Определить мое местоположение"
            )
        }
    }
}
