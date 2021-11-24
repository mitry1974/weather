package com.example.weather.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities_list")
data class CitiesListEntity(
    @PrimaryKey val id: Int,
    val name: String?,
    val lat: Double,
    val lon: Double,
    val isFavorite: Boolean = false
)
