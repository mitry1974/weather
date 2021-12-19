package com.example.weather.data.repository.citiesWeather

import com.example.weather.data.local.database.CitiesWeatherDatabase
import com.example.weather.data.local.database.entity.CityWeatherEntity
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(private val database: CitiesWeatherDatabase) {
    fun getCitiesWeather(citiesIds: List<Int>): List<CityWeatherEntity> =
        database.citiesWeatherDao().getCitiesWeather(citiesIds)

    fun insertCitiesWeather(citiesWeather: List<CityWeatherEntity>): List<CityWeatherEntity>? =
        if (database.citiesWeatherDao().insertCitiesWeather(citiesWeather).size > 1) {
            citiesWeather
        } else {
            null
        }

    fun insertCityWeather(cityWeather: CityWeatherEntity): CityWeatherEntity? =
        if (database.citiesWeatherDao().insertCityWeather(cityWeather) > 1) {
            cityWeather
        } else {
            null
        }

    fun deleteCityWeather(cityId: Int) =
        database.citiesWeatherDao().deleteCityWeather(cityId)
}