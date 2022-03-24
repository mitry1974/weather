package com.example.weather.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.data.local.database.Converters

@Entity(tableName = "forecast_city_table")
@TypeConverters(Converters::class)
data class CityForecastEntity(

    @field:PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "temp")
    val temp: Double? = null,

    @ColumnInfo(name = "icon")
    val icon: String? = null,

    @ColumnInfo(name = "time")
    val time: String? = null,

    @ColumnInfo(name = "forecastRows")
    val forecastRows: List<ForecastRow>? = null,

    )

class ForecastRow(
    @ColumnInfo(name = "main")
    val main: String? = null,

    @ColumnInfo(name = "time")
    val date: String? = null,

    weatherName: String?,
    temp: Double?,
    pressure: Int?,
    humidity: Int?,
    clouds: Int?,
    wind: Double?,
    iconFileName: String?

) : BaseWeatherInfo(weatherName, temp, pressure, humidity, clouds, wind, iconFileName)


