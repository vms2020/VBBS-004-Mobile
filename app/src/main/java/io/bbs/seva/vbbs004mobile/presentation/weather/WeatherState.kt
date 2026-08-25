package io.bbs.seva.vbbs004mobile.presentation.weather
// presentation/weather/WeatherState.kt

import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null
)
