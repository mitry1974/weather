package com.example.weather.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.R
import com.example.weather.api.model.CityWeatherResponse
import com.example.weather.util.*

@Entity(tableName = "cities_weather")
data class CityWeatherEntity(
    @PrimaryKey
    val cityId: Int?,
    val name: String?,
    val country: String?,
    val weatherName: String?,
    val temp: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val wind: Double?,
    val clouds: Int?,
    val iconFileName: String?
) {
    constructor(cityWeatherResponse: CityWeatherResponse, iconFileName: String?) : this(
        cityId = cityWeatherResponse.id,
        name = cityWeatherResponse.name,
        country = cityWeatherResponse.sys?.country,
        weatherName = cityWeatherResponse.weather?.get(0)?.description,
        temp = cityWeatherResponse.main?.temp,
        humidity = cityWeatherResponse.main?.humidity,
        pressure = cityWeatherResponse.main?.pressure,
        wind = cityWeatherResponse.wind?.speed,
        clouds = cityWeatherResponse.clouds?.all,
        iconFileName = iconFileName ?: "",
    )

    val fullCityName: String
        get() = "$name, $country"

    val propertiesList: List<WeatherItem?>
        get() =
            listOf(
                temp?.let {
                    WeatherItem("temp", temp.celsius(), R.mipmap.ic_temperature_background)
                },
                humidity?.let {
                    WeatherItem("humidity", humidity.percent(), R.mipmap.ic_humidity_background)
                },
                pressure?.let{
                    WeatherItem ("pressure", pressure.hpaInHg(), R.mipmap.ic_airpressure_background)
                },
                wind?.let{
                    WeatherItem("wind", wind.speed(), R.mipmap.ic_wind_background)
                },
                clouds?.let {
                    WeatherItem("clouds", clouds.percent(), R.mipmap.ic_clouds_background)
                })
}

data class WeatherItem(
    val key: String,
    val value: String,
    val icon: Int
)