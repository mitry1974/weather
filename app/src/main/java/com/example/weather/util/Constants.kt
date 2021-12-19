package com.example.weather.util

import com.example.weather.BuildConfig

object Constants {
    const val GENERIC_ERROR = "Произошла какая-то непредвиденная ошибка"

    const val EXTRA_CITY_NAME = "extra_city_name"
    const val EXTRA_CITY_ID = "extra_city_id"
    const val WEATHER_API_KEY = BuildConfig.WEATHER_API_KEY
    const val WEATHER_API_UNITS = "metric"
    const val BASE_URL_RETROFIT_API: String = BuildConfig.SERVER_URL
    const val ICONS_PATH = "icons"
}