package io.bbs.seva.vbbs004mobile.presentation.screens.weather
// presentation/screens/weather/WeatherState.kt

import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.usecase.WeatherDashboard

data class WeatherState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val weather: WeatherDashboard? = null,
    val error: AppError? = null
)
