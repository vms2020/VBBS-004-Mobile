package io.bbs.seva.vbbs004mobile.domain.usecase

// domain/usecase/GetWeatherUseCase.kt
import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather
import io.bbs.seva.vbbs004mobile.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Weather = repository.getCurrentWeather()
}
