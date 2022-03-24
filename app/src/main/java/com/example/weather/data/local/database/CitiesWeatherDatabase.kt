package com.example.weather.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.data.local.database.dao.CitiesListDao
import com.example.weather.data.local.database.dao.CityForecastDao
import com.example.weather.data.local.database.dao.CityWeatherDao
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.local.database.entity.CityForecastEntity
import com.example.weather.data.local.database.entity.CityWeatherEntity

@Database(
    entities = [CitiesListEntity::class, CityWeatherEntity::class, CityForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CitiesWeatherDatabase : RoomDatabase() {
    abstract fun citiesListDao(): CitiesListDao
    abstract fun citiesWeatherDao(): CityWeatherDao
    abstract fun citiesForecastDao(): CityForecastDao

    companion object {
        fun buildDatabase(context: Context): CitiesWeatherDatabase =
            Room.databaseBuilder(context, CitiesWeatherDatabase::class.java, "Cities").build()
    }
}
