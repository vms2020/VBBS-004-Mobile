package io.bbs.seva.vbbs004mobile.domain.repository
// domain/repository/WeatherRepository.kt

import io.bbs.seva.vbbs004mobile.domain.model.weather.Forecast
import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double = 55.6107, lon: Double = 37.7597): Weather
    suspend fun getWeatherForecast(lat: Double = 55.6107, lon: Double = 37.7597): Forecast
}
