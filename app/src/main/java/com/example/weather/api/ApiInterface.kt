package com.example.weather.api

import com.example.weather.api.model.*
import com.example.weather.data.local.database.entity.CityNameResponse
import com.example.weather.util.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiInterface {
    @Streaming
    @GET("https://bulk.openweathermap.org/sample/city.list.json.gz")
    suspend fun getCitiesListFile(): Response<ResponseBody>

    @GET("https://api.openweathermap.org/geo/1.0/reverse")
    suspend fun getCityNamesByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = Constants.WEATHER_API_KEY
    ): Response<List<CityNameResponse>>

    @GET("weather")
    suspend fun getCityWeatherById(
        @Query("id") id: Int,
        @Query("units") units: String = Constants.WEATHER_API_UNITS,
        @Query("appid") appid: String = Constants.WEATHER_API_KEY
    ): Response<CityWeatherResponse>

    @GET("https://openweathermap.org/img/wn/{icon}@2x.png")
    suspend fun getCityWeatherIcon(
        @Path("icon")
        icon: String
    ): Response<ResponseBody>

    @GET("onecall")
    suspend fun getCityForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = Constants.WEATHER_API_UNITS,
        @Query("exclude") exclude: String = "weather,minutely,hourly",
        @Query("appid") appid: String = Constants.WEATHER_API_KEY
    ): Response<CityForecastResponse>

//    // Get city historical data
//    // https://api.openweathermap.org/data/2.5/onecall/timemachine?lat={lat}&lon={lon}&dt={time}&appid={API key}
//    @GET("onecall/timemachine")
//    suspend fun historicalWeather(
//        @Query("lat") lat: String,
//        @Query("lon") lon: String,
//        @Query("dt") dt: String,
//        @Query("lang") lang: String,
//        @Query("appid") appid: String,
//    ): Response<HistoricalWeatherResponse>
}