package io.bbs.seva.vbbs004mobile.data.repository

// data/repository/WeatherRepositoryImpl.kt
import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.data.remote.dto.weather.OpenWeatherMapWeatherDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.weather.forecast.OpenWeatherMapForecastDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.weather.forecast.toDomain
import io.bbs.seva.vbbs004mobile.data.remote.dto.weather.toDomain
import io.bbs.seva.vbbs004mobile.domain.model.weather.Forecast
import io.bbs.seva.vbbs004mobile.domain.model.weather.Weather
import io.bbs.seva.vbbs004mobile.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val client: HttpClient // This is the client configured with the Ktor Auth plugin
) : WeatherRepository {

    private val baseUrl = BuildConfig.BASE_URL

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Weather {
        // Ktor automatically appends the Bearer token under the hood
        val dto = client.get("${baseUrl}weather") {
            parameter("lat", lat)
            parameter("lon", lon)
        }.body<OpenWeatherMapWeatherDto>()
        return dto.toDomain()
//        Weather(
//            temperature = dto.main?.temp ?: 9999.99,
//            condition = dto.weather?.get(0)?.description ?: "xxx"
//        )
    }

    override suspend fun getWeatherForecast(
        lat: Double,
        lon: Double
    ): Forecast {
        val dto = client.get("${baseUrl}weather/forecast"){
            parameter("lat", lat)
            parameter("lon", lon)
        }.body<OpenWeatherMapForecastDto>()
        return Forecast(items = dto.list?.mapNotNull { w ->
            w?.toDomain()
//            Weather(
//                it?.main?.temp ?: 9999.99,
//                it?.weather?.get(0)?.description ?: "xxx"
//            )
        } ?: listOf())
    }
}
