package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.LocalTopBarController
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarAction
import io.bbs.seva.vbbs004mobile.core.designsystem.topbar.TopBarState
import io.bbs.seva.vbbs004mobile.feature.weather.R
import io.bbs.seva.vbbs004mobile.presentation.screens.weather.WeatherScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.weather.WeatherViewModel

fun EntryProviderScope<NavKey>.weatherEntryBuilder(navigator: AppNavigator) {
    entry<Destination.Weather> {
        val viewModel: WeatherViewModel = hiltViewModel()
        val topBar = LocalTopBarController.current

        val title = stringResource(R.string.weather_title)
        val mapLabel = stringResource(R.string.weather_show_map)
        val state by viewModel.state.collectAsStateWithLifecycle()

        SideEffect {
            topBar?.set(
                TopBarState(
                    title = title,
                    actions = listOf(
                        TopBarAction.MyTopBarActionIconButton(
                            icon = Icons.Filled.LocationOn,
                            contentDescription = mapLabel,
                            onClick = { navigator.navigate(Destination.GeoLocationDest(0.0, 0.0)) },
                        ),
                    ),
                ),
                owner = Destination.Weather
            )
        }
        DisposableEffect(Unit) {
            onDispose {
                topBar?.clear(Destination.Weather)
            }
        }

        WeatherScreen(
            viewModel = viewModel,
        )
    }
}
