package io.bbs.seva.vbbs004mobile.data.remote.dto.weather.forecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "cod": "200",
    "message": 0,
    "cnt": 40,
    "list": [
        {
            "dt": 1787670000,
            "main": {
                "temp": 17.42,
                "feels_like": 17.02,
                "temp_min": 17.37,
                "temp_max": 17.42,
                "pressure": 1012,
                "sea_level": 1012,
                "grnd_level": 996,
                "humidity": 69,
                "temp_kf": 0.05,
                "dew_point": 11.68
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 5.1,
                "deg": 288,
                "gust": 9.66
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-25 15:00:00"
        },
        {
            "dt": 1787680800,
            "main": {
                "temp": 16.27,
                "feels_like": 16.02,
                "temp_min": 15.68,
                "temp_max": 16.27,
                "pressure": 1013,
                "sea_level": 1013,
                "grnd_level": 997,
                "humidity": 79,
                "temp_kf": 0.59,
                "dew_point": 12.63
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 97
            },
            "wind": {
                "speed": 3.7,
                "deg": 279,
                "gust": 7.11
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-25 18:00:00"
        },
        {
            "dt": 1787691600,
            "main": {
                "temp": 14.14,
                "feels_like": 14.01,
                "temp_min": 14.14,
                "temp_max": 14.14,
                "pressure": 1015,
                "sea_level": 1015,
                "grnd_level": 997,
                "humidity": 92,
                "temp_kf": 0,
                "dew_point": 10.88
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 4.39,
                "deg": 278,
                "gust": 8.88
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-25 21:00:00"
        },
        {
            "dt": 1787702400,
            "main": {
                "temp": 14,
                "feels_like": 13.7,
                "temp_min": 14,
                "temp_max": 14,
                "pressure": 1015,
                "sea_level": 1015,
                "grnd_level": 998,
                "humidity": 86,
                "temp_kf": 0,
                "dew_point": 10.19
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 4.99,
                "deg": 297,
                "gust": 8.55
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-26 00:00:00"
        },
        {
            "dt": 1787713200,
            "main": {
                "temp": 13.79,
                "feels_like": 13.44,
                "temp_min": 13.79,
                "temp_max": 13.79,
                "pressure": 1016,
                "sea_level": 1016,
                "grnd_level": 998,
                "humidity": 85,
                "temp_kf": 0,
                "dew_point": 10.06
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 97
            },
            "wind": {
                "speed": 4.61,
                "deg": 290,
                "gust": 9.14
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-26 03:00:00"
        },
        {
            "dt": 1787724000,
            "main": {
                "temp": 13.21,
                "feels_like": 12.86,
                "temp_min": 13.21,
                "temp_max": 13.21,
                "pressure": 1017,
                "sea_level": 1017,
                "grnd_level": 999,
                "humidity": 87,
                "temp_kf": 0,
                "dew_point": 11.12
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 98
            },
            "wind": {
                "speed": 4.53,
                "deg": 306,
                "gust": 8.55
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-26 06:00:00"
        },
        {
            "dt": 1787734800,
            "main": {
                "temp": 15.87,
                "feels_like": 15.44,
                "temp_min": 15.87,
                "temp_max": 15.87,
                "pressure": 1018,
                "sea_level": 1018,
                "grnd_level": 1001,
                "humidity": 74,
                "temp_kf": 0,
                "dew_point": 11.25
            },
            "weather": [
                {
                    "id": 500,
                    "main": "Rain",
                    "description": "light rain",
                    "icon": "10d"
                }
            ],
            "clouds": {
                "all": 99
            },
            "wind": {
                "speed": 4.94,
                "deg": 337,
                "gust": 6.33
            },
            "visibility": 10000,
            "pop": 0.2,
            "rain": {
                "3h": 0.17
            },
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-26 09:00:00"
        },
        {
            "dt": 1787745600,
            "main": {
                "temp": 14.84,
                "feels_like": 14.39,
                "temp_min": 14.84,
                "temp_max": 14.84,
                "pressure": 1020,
                "sea_level": 1020,
                "grnd_level": 1002,
                "humidity": 77,
                "temp_kf": 0,
                "dew_point": 10.78
            },
            "weather": [
                {
                    "id": 500,
                    "main": "Rain",
                    "description": "light rain",
                    "icon": "10d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 4.32,
                "deg": 338,
                "gust": 6.75
            },
            "visibility": 10000,
            "pop": 0.2,
            "rain": {
                "3h": 0.15
            },
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-26 12:00:00"
        },
        {
            "dt": 1787756400,
            "main": {
                "temp": 15.37,
                "feels_like": 14.84,
                "temp_min": 15.37,
                "temp_max": 15.37,
                "pressure": 1021,
                "sea_level": 1021,
                "grnd_level": 1003,
                "humidity": 72,
                "temp_kf": 0,
                "dew_point": 10.28
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 93
            },
            "wind": {
                "speed": 4.63,
                "deg": 339,
                "gust": 7
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-26 15:00:00"
        },
        {
            "dt": 1787767200,
            "main": {
                "temp": 12.81,
                "feels_like": 12.05,
                "temp_min": 12.81,
                "temp_max": 12.81,
                "pressure": 1023,
                "sea_level": 1023,
                "grnd_level": 1005,
                "humidity": 73,
                "temp_kf": 0,
                "dew_point": 8.05
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 86
            },
            "wind": {
                "speed": 3.47,
                "deg": 342,
                "gust": 6.75
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-26 18:00:00"
        },
        {
            "dt": 1787778000,
            "main": {
                "temp": 10.64,
                "feels_like": 9.95,
                "temp_min": 10.64,
                "temp_max": 10.64,
                "pressure": 1023,
                "sea_level": 1023,
                "grnd_level": 1006,
                "humidity": 84,
                "temp_kf": 0,
                "dew_point": 7.93
            },
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01n"
                }
            ],
            "clouds": {
                "all": 0
            },
            "wind": {
                "speed": 2.34,
                "deg": 336,
                "gust": 4.46
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-26 21:00:00"
        },
        {
            "dt": 1787788800,
            "main": {
                "temp": 10.57,
                "feels_like": 9.95,
                "temp_min": 10.57,
                "temp_max": 10.57,
                "pressure": 1024,
                "sea_level": 1024,
                "grnd_level": 1006,
                "humidity": 87,
                "temp_kf": 0,
                "dew_point": 8.51
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02n"
                }
            ],
            "clouds": {
                "all": 19
            },
            "wind": {
                "speed": 2.23,
                "deg": 336,
                "gust": 3.88
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-27 00:00:00"
        },
        {
            "dt": 1787799600,
            "main": {
                "temp": 10,
                "feels_like": 9.19,
                "temp_min": 10,
                "temp_max": 10,
                "pressure": 1025,
                "sea_level": 1025,
                "grnd_level": 1007,
                "humidity": 89,
                "temp_kf": 0,
                "dew_point": 8.15
            },
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                }
            ],
            "clouds": {
                "all": 5
            },
            "wind": {
                "speed": 2,
                "deg": 346,
                "gust": 2.79
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-27 03:00:00"
        },
        {
            "dt": 1787810400,
            "main": {
                "temp": 15.27,
                "feels_like": 14.58,
                "temp_min": 15.27,
                "temp_max": 15.27,
                "pressure": 1026,
                "sea_level": 1026,
                "grnd_level": 1008,
                "humidity": 66,
                "temp_kf": 0,
                "dew_point": 8.85
            },
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                }
            ],
            "clouds": {
                "all": 5
            },
            "wind": {
                "speed": 2.73,
                "deg": 4,
                "gust": 4.13
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-27 06:00:00"
        },
        {
            "dt": 1787821200,
            "main": {
                "temp": 19.38,
                "feels_like": 18.65,
                "temp_min": 19.38,
                "temp_max": 19.38,
                "pressure": 1025,
                "sea_level": 1025,
                "grnd_level": 1008,
                "humidity": 49,
                "temp_kf": 0,
                "dew_point": 8.36
            },
            "weather": [
                {
                    "id": 800,
                    "main": "Clear",
                    "description": "clear sky",
                    "icon": "01d"
                }
            ],
            "clouds": {
                "all": 8
            },
            "wind": {
                "speed": 2.98,
                "deg": 14,
                "gust": 4.3
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-27 09:00:00"
        },
        {
            "dt": 1787832000,
            "main": {
                "temp": 19.16,
                "feels_like": 18.41,
                "temp_min": 19.16,
                "temp_max": 19.16,
                "pressure": 1025,
                "sea_level": 1025,
                "grnd_level": 1007,
                "humidity": 49,
                "temp_kf": 0,
                "dew_point": 8.16
            },
            "weather": [
                {
                    "id": 802,
                    "main": "Clouds",
                    "description": "scattered clouds",
                    "icon": "03d"
                }
            ],
            "clouds": {
                "all": 46
            },
            "wind": {
                "speed": 2.88,
                "deg": 17,
                "gust": 4.17
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-27 12:00:00"
        },
        {
            "dt": 1787842800,
            "main": {
                "temp": 18.62,
                "feels_like": 17.97,
                "temp_min": 18.62,
                "temp_max": 18.62,
                "pressure": 1025,
                "sea_level": 1025,
                "grnd_level": 1008,
                "humidity": 55,
                "temp_kf": 0,
                "dew_point": 9.22
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 1.6,
                "deg": 11,
                "gust": 2.46
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-27 15:00:00"
        },
        {
            "dt": 1787853600,
            "main": {
                "temp": 15.4,
                "feels_like": 14.85,
                "temp_min": 15.4,
                "temp_max": 15.4,
                "pressure": 1026,
                "sea_level": 1026,
                "grnd_level": 1009,
                "humidity": 71,
                "temp_kf": 0,
                "dew_point": 10.18
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 93
            },
            "wind": {
                "speed": 3.05,
                "deg": 27,
                "gust": 6.79
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-27 18:00:00"
        },
        {
            "dt": 1787864400,
            "main": {
                "temp": 11.95,
                "feels_like": 11.52,
                "temp_min": 11.95,
                "temp_max": 11.95,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 89,
                "temp_kf": 0,
                "dew_point": 10.01
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02n"
                }
            ],
            "clouds": {
                "all": 17
            },
            "wind": {
                "speed": 2.58,
                "deg": 64,
                "gust": 4.64
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-27 21:00:00"
        },
        {
            "dt": 1787875200,
            "main": {
                "temp": 10.85,
                "feels_like": 10.29,
                "temp_min": 10.85,
                "temp_max": 10.85,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 88,
                "temp_kf": 0,
                "dew_point": 8.96
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02n"
                }
            ],
            "clouds": {
                "all": 16
            },
            "wind": {
                "speed": 1.71,
                "deg": 53,
                "gust": 1.99
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-28 00:00:00"
        },
        {
            "dt": 1787886000,
            "main": {
                "temp": 10.21,
                "feels_like": 9.58,
                "temp_min": 10.21,
                "temp_max": 10.21,
                "pressure": 1029,
                "sea_level": 1029,
                "grnd_level": 1011,
                "humidity": 88,
                "temp_kf": 0,
                "dew_point": 8.31
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02d"
                }
            ],
            "clouds": {
                "all": 20
            },
            "wind": {
                "speed": 1.62,
                "deg": 36,
                "gust": 2.03
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-28 03:00:00"
        },
        {
            "dt": 1787896800,
            "main": {
                "temp": 14.58,
                "feels_like": 13.76,
                "temp_min": 14.58,
                "temp_max": 14.58,
                "pressure": 1029,
                "sea_level": 1029,
                "grnd_level": 1011,
                "humidity": 64,
                "temp_kf": 0,
                "dew_point": 7.88
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02d"
                }
            ],
            "clouds": {
                "all": 18
            },
            "wind": {
                "speed": 1.64,
                "deg": 56,
                "gust": 2.01
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-28 06:00:00"
        },
        {
            "dt": 1787907600,
            "main": {
                "temp": 18.35,
                "feels_like": 17.47,
                "temp_min": 18.35,
                "temp_max": 18.35,
                "pressure": 1029,
                "sea_level": 1029,
                "grnd_level": 1011,
                "humidity": 47,
                "temp_kf": 0,
                "dew_point": 6.7
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "few clouds",
                    "icon": "02d"
                }
            ],
            "clouds": {
                "all": 12
            },
            "wind": {
                "speed": 1.94,
                "deg": 9,
                "gust": 2.91
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-28 09:00:00"
        },
        {
            "dt": 1787918400,
            "main": {
                "temp": 20.35,
                "feels_like": 19.43,
                "temp_min": 20.35,
                "temp_max": 20.35,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 38,
                "temp_kf": 0,
                "dew_point": 5.77
            },
            "weather": [
                {
                    "id": 802,
                    "main": "Clouds",
                    "description": "scattered clouds",
                    "icon": "03d"
                }
            ],
            "clouds": {
                "all": 25
            },
            "wind": {
                "speed": 2.51,
                "deg": 341,
                "gust": 3.93
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-28 12:00:00"
        },
        {
            "dt": 1787929200,
            "main": {
                "temp": 18.47,
                "feels_like": 17.55,
                "temp_min": 18.47,
                "temp_max": 18.47,
                "pressure": 1027,
                "sea_level": 1027,
                "grnd_level": 1010,
                "humidity": 45,
                "temp_kf": 0,
                "dew_point": 6.25
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 88
            },
            "wind": {
                "speed": 2.8,
                "deg": 353,
                "gust": 3.79
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-28 15:00:00"
        },
        {
            "dt": 1787940000,
            "main": {
                "temp": 13.84,
                "feels_like": 12.87,
                "temp_min": 13.84,
                "temp_max": 13.84,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 61,
                "temp_kf": 0,
                "dew_point": 6.33
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 74
            },
            "wind": {
                "speed": 1.77,
                "deg": 77,
                "gust": 2.44
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-28 18:00:00"
        },
        {
            "dt": 1787950800,
            "main": {
                "temp": 11.93,
                "feels_like": 10.82,
                "temp_min": 11.93,
                "temp_max": 11.93,
                "pressure": 1029,
                "sea_level": 1029,
                "grnd_level": 1011,
                "humidity": 63,
                "temp_kf": 0,
                "dew_point": 5.11
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 69
            },
            "wind": {
                "speed": 1.61,
                "deg": 108,
                "gust": 1.96
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-28 21:00:00"
        },
        {
            "dt": 1787961600,
            "main": {
                "temp": 10.84,
                "feels_like": 9.76,
                "temp_min": 10.84,
                "temp_max": 10.84,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 68,
                "temp_kf": 0,
                "dew_point": 4.98
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 70
            },
            "wind": {
                "speed": 1.43,
                "deg": 151,
                "gust": 1.65
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-29 00:00:00"
        },
        {
            "dt": 1787972400,
            "main": {
                "temp": 12.5,
                "feels_like": 11.37,
                "temp_min": 12.5,
                "temp_max": 12.5,
                "pressure": 1028,
                "sea_level": 1028,
                "grnd_level": 1010,
                "humidity": 60,
                "temp_kf": 0,
                "dew_point": 5.01
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 89
            },
            "wind": {
                "speed": 1.39,
                "deg": 193,
                "gust": 1.72
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-29 03:00:00"
        },
        {
            "dt": 1787983200,
            "main": {
                "temp": 14.48,
                "feels_like": 13.45,
                "temp_min": 14.48,
                "temp_max": 14.48,
                "pressure": 1027,
                "sea_level": 1027,
                "grnd_level": 1009,
                "humidity": 56,
                "temp_kf": 0,
                "dew_point": 5.82
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 93
            },
            "wind": {
                "speed": 1.97,
                "deg": 205,
                "gust": 2.85
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-29 06:00:00"
        },
        {
            "dt": 1787994000,
            "main": {
                "temp": 17.16,
                "feels_like": 16.37,
                "temp_min": 17.16,
                "temp_max": 17.16,
                "pressure": 1026,
                "sea_level": 1026,
                "grnd_level": 1009,
                "humidity": 55,
                "temp_kf": 0,
                "dew_point": 8
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 2.28,
                "deg": 195,
                "gust": 3.22
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-29 09:00:00"
        },
        {
            "dt": 1788004800,
            "main": {
                "temp": 21.01,
                "feels_like": 20.37,
                "temp_min": 21.01,
                "temp_max": 21.01,
                "pressure": 1024,
                "sea_level": 1024,
                "grnd_level": 1007,
                "humidity": 46,
                "temp_kf": 0,
                "dew_point": 8.95
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 85
            },
            "wind": {
                "speed": 2.52,
                "deg": 192,
                "gust": 2.99
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-29 12:00:00"
        },
        {
            "dt": 1788015600,
            "main": {
                "temp": 19.5,
                "feels_like": 18.84,
                "temp_min": 19.5,
                "temp_max": 19.5,
                "pressure": 1023,
                "sea_level": 1023,
                "grnd_level": 1005,
                "humidity": 51,
                "temp_kf": 0,
                "dew_point": 9.03
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 55
            },
            "wind": {
                "speed": 2.45,
                "deg": 214,
                "gust": 4.15
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-29 15:00:00"
        },
        {
            "dt": 1788026400,
            "main": {
                "temp": 17.62,
                "feels_like": 16.87,
                "temp_min": 17.62,
                "temp_max": 17.62,
                "pressure": 1022,
                "sea_level": 1022,
                "grnd_level": 1004,
                "humidity": 55,
                "temp_kf": 0,
                "dew_point": 8.31
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 78
            },
            "wind": {
                "speed": 2.83,
                "deg": 197,
                "gust": 6.08
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-29 18:00:00"
        },
        {
            "dt": 1788037200,
            "main": {
                "temp": 14.13,
                "feels_like": 13.19,
                "temp_min": 14.13,
                "temp_max": 14.13,
                "pressure": 1021,
                "sea_level": 1021,
                "grnd_level": 1004,
                "humidity": 61,
                "temp_kf": 0,
                "dew_point": 6.75
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 3.08,
                "deg": 191,
                "gust": 5.93
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-29 21:00:00"
        },
        {
            "dt": 1788048000,
            "main": {
                "temp": 13.44,
                "feels_like": 12.3,
                "temp_min": 13.44,
                "temp_max": 13.44,
                "pressure": 1020,
                "sea_level": 1020,
                "grnd_level": 1003,
                "humidity": 56,
                "temp_kf": 0,
                "dew_point": 4.78
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04n"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 2.95,
                "deg": 173,
                "gust": 7.04
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2026-08-30 00:00:00"
        },
        {
            "dt": 1788058800,
            "main": {
                "temp": 13.38,
                "feels_like": 12.21,
                "temp_min": 13.38,
                "temp_max": 13.38,
                "pressure": 1019,
                "sea_level": 1019,
                "grnd_level": 1001,
                "humidity": 55,
                "temp_kf": 0,
                "dew_point": 4.67
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 3.28,
                "deg": 156,
                "gust": 9.11
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-30 03:00:00"
        },
        {
            "dt": 1788069600,
            "main": {
                "temp": 15.27,
                "feels_like": 14.21,
                "temp_min": 15.27,
                "temp_max": 15.27,
                "pressure": 1017,
                "sea_level": 1017,
                "grnd_level": 1000,
                "humidity": 52,
                "temp_kf": 0,
                "dew_point": 5.46
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 4.28,
                "deg": 150,
                "gust": 10.48
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-30 06:00:00"
        },
        {
            "dt": 1788080400,
            "main": {
                "temp": 16.99,
                "feels_like": 16.08,
                "temp_min": 16.99,
                "temp_max": 16.99,
                "pressure": 1016,
                "sea_level": 1016,
                "grnd_level": 999,
                "humidity": 51,
                "temp_kf": 0,
                "dew_point": 6.83
            },
            "weather": [
                {
                    "id": 804,
                    "main": "Clouds",
                    "description": "overcast clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 4.14,
                "deg": 182,
                "gust": 7.38
            },
            "visibility": 10000,
            "pop": 0,
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-30 09:00:00"
        },
        {
            "dt": 1788091200,
            "main": {
                "temp": 14.04,
                "feels_like": 13.51,
                "temp_min": 14.04,
                "temp_max": 14.04,
                "pressure": 1016,
                "sea_level": 1016,
                "grnd_level": 998,
                "humidity": 77,
                "temp_kf": 0,
                "dew_point": 10.03
            },
            "weather": [
                {
                    "id": 500,
                    "main": "Rain",
                    "description": "light rain",
                    "icon": "10d"
                }
            ],
            "clouds": {
                "all": 100
            },
            "wind": {
                "speed": 2.87,
                "deg": 183,
                "gust": 5.8
            },
            "visibility": 10000,
            "pop": 0.2,
            "rain": {
                "3h": 0.36
            },
            "sys": {
                "pod": "d"
            },
            "dt_txt": "2026-08-30 12:00:00"
        }
    ],
    "city": {
        "id": 461835,
        "name": "Zyablikovo",
        "coord": {
            "lat": 55.6107,
            "lon": 37.7597
        },
        "country": "RU",
        "population": 0,
        "timezone": 10800,
        "sunrise": 1787624413,
        "sunset": 1787676149
    }
}
*/
@Serializable
data class MainDto(
    @SerialName("temp")
    val temp: Double? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("temp_min")
    val tempMin: Double? = null,
    @SerialName("temp_max")
    val tempMax: Double? = null,
    @SerialName("pressure")
    val pressure: Int? = null,
    @SerialName("sea_level")
    val seaLevel: Int? = null,
    @SerialName("grnd_level")
    val grndLevel: Int? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("temp_kf")
    val tempKf: Double? = null,
    @SerialName("dew_point")
    val dewPoint: Double? = null
)
