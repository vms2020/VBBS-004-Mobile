package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.domain.constant.LocationConstants
import io.bbs.seva.vbbs004mobile.presentation.screens.osm.OsmPickerScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.osm.OsmPickerViewModel

// feature/location/.../presentation/navigation/LocationEntries.kt
fun EntryProviderScope<NavKey>.locationEntryBuilder(navigator: AppNavigator) {
    entry<Destination.GeoLocationDest> {
        val viewModel: OsmPickerViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        if (state.isLoading) {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            return@entry
        }
        OsmPickerScreen(
            initialLatitude = state.saved?.lat ?: LocationConstants.DEFAULT_LOCATION.lat,
            initialLongitude = state.saved?.lon ?: LocationConstants.DEFAULT_LOCATION.lon,
            onForceGpsRequest = viewModel::getFreshGpsLocation,
            onLocationSelected = { a, b ->
                viewModel.saveLocation(a, b)
                navigator.back()          // ← identical semantics, feature-safe
            },
            onCancelSelected = { navigator.back() },
        )
    }
}
