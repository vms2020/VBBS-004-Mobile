package io.bbs.seva.vbbs004mobile.presentation.weather
// presentation/weather/WeatherState.kt

import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather
import io.bbs.seva.vbbs004mobile.domain.usecase.WeatherDashboard

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: WeatherDashboard? = null,
    val error: String? = null
)
