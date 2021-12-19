package com.example.weather.data.local.database.dao

import androidx.room.*
import com.example.weather.data.local.database.entity.CityWeatherEntity

@Dao
interface CityWeatherDao {
    @Query("SELECT * FROM cities_weather WHERE cityId IN (:citiesId)")
    fun getCitiesWeather(citiesId: List<Int>): List<CityWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCitiesWeather(citiesWeather: List<CityWeatherEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCityWeather(cityWeather: CityWeatherEntity): Long

    @Query("DELETE FROM cities_weather WHERE cityId = :cityId")
    fun deleteCityWeather(cityId: Int)
}