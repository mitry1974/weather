package com.example.weather.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.data.local.database.entity.CityForecastEntity

@Dao
interface CityForecastDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCityForecast(forecastCity : CityForecastEntity) : Long

    @Query("select * from forecast_city_table where id =:id")
    suspend fun getCityForecast(id : Int): CityForecastEntity

    @Query("select COUNT(*) FROM forecast_city_table")
    suspend fun getSizeForecastCities(): Int

    @Query("delete from forecast_city_table")
    suspend fun deleteAllForecasts()

    @Query("delete from forecast_city_table where id =:id")
    suspend fun deleteCityForecast(id : Int)
}