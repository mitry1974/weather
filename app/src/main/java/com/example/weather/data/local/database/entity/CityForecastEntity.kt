package com.example.weather.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

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


data class ForecastRow(

    @ColumnInfo(name = "main")
    val main: String? = null,

    @ColumnInfo(name = "icon")
    val icon: String? = null,

    @ColumnInfo(name = "time")
    val date: String? = null,

    @ColumnInfo(name = "temp")
    val temp: Double? = null,

    @ColumnInfo(name = "pressure")
    val pressure: Int? = null,

    @ColumnInfo(name = "humidity")
    val humidity: Int? = null,

    @ColumnInfo(name = "clouds")
    val clouds: Int? = null,

    @ColumnInfo(name = "wind")
    val wind: Double? = null

)

