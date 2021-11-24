package com.example.weather.api

import com.example.weather.api.model.City
import com.example.weather.api.model.Forecast
import com.example.weather.api.model.HistoricalWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("http://bulk.openweathermap.org/sample/city.list.json.gz")
    suspend fun citiesList(): Response<List<City>>

    // "https://api.openweathermap.org/data/2.5/weather?q=Moscow&appid=08733d4c33fc2c5ef77ef3273cc7a148"

    // Get city weather
    @GET("weather/{id}")
    suspend fun cityWeather(
        @Query("id") id: String
    ): Response<City>

    // Get city forecast 2 days
    // https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}
    @GET("onecall")
    suspend fun forecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String,
        ): Response<Forecast>

    // Get city historical data
    // https://api.openweathermap.org/data/2.5/onecall/timemachine?lat={lat}&lon={lon}&dt={time}&appid={API key}
    @GET("onecall/timemachine")
    suspend fun historicalWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("dt") dt: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String,
    ): Response<HistoricalWeatherResponse>
}