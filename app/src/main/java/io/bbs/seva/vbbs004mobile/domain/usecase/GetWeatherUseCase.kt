package io.bbs.seva.vbbs004mobile.domain.usecase

// domain/usecase/GetWeatherUseCase.kt
import io.bbs.seva.vbbs004mobile.domain.model.weather.Forecast
import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather
import io.bbs.seva.vbbs004mobile.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import javax.inject.Inject
import kotlinx.coroutines.coroutineScope

data class WeatherDashboard(
    val current: Weather,
    val forecast: Forecast
)

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    //suspend operator fun invoke(): Weather = repository.getCurrentWeather()

    suspend operator fun invoke(lat: Double = 55.6107, lon: Double = 37.7597): WeatherDashboard = coroutineScope {
        val currentDeferred = async { repository.getCurrentWeather(lat, lon) }
        val forecastDeferred = async { repository.getWeatherForecast(lat, lon) }

        WeatherDashboard(
            current = currentDeferred.await(),
            forecast = forecastDeferred.await()
        )
    }
}
