package com.example.weather.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CitiesListEntity::class], version = 1, exportSchema = false)
abstract class CitiesWeatherDatabase : RoomDatabase() {
    abstract fun citiesListDao(): CitiesListDao

    companion object {
        fun buildDatabase(context: Context): CitiesWeatherDatabase =
            Room.databaseBuilder(context, CitiesWeatherDatabase::class.java, "Cities").build()
    }
}
