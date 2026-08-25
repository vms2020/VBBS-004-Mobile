package io.bbs.seva.vbbs004mobile.data.remote.dto.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "coord": {
        "lon": 37.7597,
        "lat": 55.6107
    },
    "weather": [
        {
            "id": 804,
            "main": "Clouds",
            "description": "overcast clouds",
            "icon": "04d"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 16.53,
        "feels_like": 16.3,
        "temp_min": 16.33,
        "temp_max": 18.77,
        "pressure": 1013,
        "humidity": 79,
        "sea_level": 1013,
        "grnd_level": 996
    },
    "visibility": 10000,
    "wind": {
        "speed": 6.33,
        "deg": 275,
        "gust": 9.98
    },
    "clouds": {
        "all": 100
    },
    "dt": 1787659179,
    "sys": {
        "type": 2,
        "id": 2000314,
        "country": "RU",
        "sunrise": 1787624413,
        "sunset": 1787676149
    },
    "timezone": 10800,
    "id": 461835,
    "name": "Zyablikovo",
    "cod": 200
}
*/
@Serializable
data class CloudsDto(
    @SerialName("all")
    val all: Int? = null
)