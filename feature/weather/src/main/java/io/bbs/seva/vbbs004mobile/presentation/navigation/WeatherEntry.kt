package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.presentation.screens.weather.WeatherScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.weather.WeatherViewModel


fun EntryProviderScope<NavKey>.weatherEntryBuilder(navigator: AppNavigator) {
    entry<Destination.Weather> {
        val viewModel: WeatherViewModel = hiltViewModel()
        WeatherScreen(
            viewModel = viewModel,
            // ← copy the EXACT lambdas from AppRoot's current Home entry,
            //   replacing backstack.add(X) with navigator.navigate(X)
            //   and backstack.removeAt(lastIndex) with navigator.back()
        )
    }
}
