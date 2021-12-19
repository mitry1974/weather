package com.example.weather.data.repository.citiesWeather

import com.example.weather.api.ApiInterface
import com.example.weather.api.BaseRemoteDataSource
import com.example.weather.api.Result
import com.example.weather.api.model.CityForecastResponse
import com.example.weather.api.model.CityWeatherResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val service: ApiInterface) :
    BaseRemoteDataSource() {

    suspend fun loadCityWeatherById(id: Int): Result<CityWeatherResponse> =
        getResult {
            service.getCityWeatherById(id)
        }

    suspend fun loadCityWeatherIcon(icon: String): Result<ResponseBody> =
        getResult {
            service.getCityWeatherIcon(icon)
        }

    suspend fun loadCityForecastByLatLon(lat: Double, lon: Double): Result<CityForecastResponse> =
        getResult {
            service.getCityForecast(lat, lon)
        }
}

