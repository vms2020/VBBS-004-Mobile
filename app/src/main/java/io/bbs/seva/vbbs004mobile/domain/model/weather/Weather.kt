package io.bbs.seva.vbbs004mobile.domain.model.weather


// domain/model/Weather.kt
data class Weather(
    val temperature: Double,
    val condition: String,
    val icon: String? = null,
    val time: Long? = null,
)

data class Forecast(val items: List<Weather>)


