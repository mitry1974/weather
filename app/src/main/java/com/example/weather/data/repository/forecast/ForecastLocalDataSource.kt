package com.example.weather.data.repository.forecast

import com.example.weather.data.local.database.CitiesWeatherDatabase
import com.example.weather.data.local.database.CityForecastEntity
import javax.inject.Inject

class ForecastLocalDataSource @Inject constructor(private val database: CitiesWeatherDatabase) {

    suspend fun getCityForecastById(cityID: Int) = database.citiesForecastDao().getCityForecast(cityID)

    suspend fun insertCityForecast(forecast: CityForecastEntity) = database.citiesForecastDao().insertCityForecast(forecast)

    suspend fun deleteForecastByCityId(cityId: Int) = database.citiesForecastDao().deleteCityForecast(cityId)
}