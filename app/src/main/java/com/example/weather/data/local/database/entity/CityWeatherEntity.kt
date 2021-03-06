package com.example.weather.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.R
import com.example.weather.api.model.CityWeatherResponse
import com.example.weather.util.*

@Entity
open class BaseWeatherInfo(
    val weatherName: String?,
    val temp: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val clouds: Int?,
    val wind: Double?,
    val iconFileName: String?,
) {
    data class WeatherItem(
        val keyId: Int,
        val value: String,
        val icon: Int
    )
    val propertiesList: List<WeatherItem?>
        get() = listOfNotNull(
            temp?.let {
                WeatherItem(R.string.temperature_weather_element1, temp.celsius(), R.mipmap.ic_temperature_background)
            },
            humidity?.let {
                WeatherItem(R.string.humidity_weather_element, humidity.percent(), R.mipmap.ic_humidity_background)
            },
            pressure?.let {
                WeatherItem(R.string.pressure_weather_element, pressure.hpaInHg(), R.mipmap.ic_airpressure_background)
            },
            wind?.let {
                WeatherItem(R.string.wind_weather_element, wind.speed(), R.mipmap.ic_wind_background)
            },
            clouds?.let {
                WeatherItem(R.string.clouds_weather_element, clouds.percent(), R.mipmap.ic_clouds_background)
            })

}

@Entity(tableName = "cities_weather")
class CityWeatherEntity(
    @PrimaryKey
    val cityId: Int?,
    val name: String?,
    val country: String?,
    weatherName: String?,
    temp: Double?,
    pressure: Int?,
    humidity: Int?,
    clouds: Int?,
    wind: Double?,
    iconFileName: String?
) : BaseWeatherInfo(weatherName, temp, pressure, humidity, clouds, wind, iconFileName) {
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
}