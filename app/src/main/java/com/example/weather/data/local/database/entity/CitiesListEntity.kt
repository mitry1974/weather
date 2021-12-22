package com.example.weather.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.api.model.CityResponse

data class CityNameResponse(val name: String, val country: String, val state: String)

data class Coordinates(
    val lat: Double,
    val lon: Double,
)

@Entity(tableName = "cities_list")
data class CitiesListEntity(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val country: String?,
    val lat: Double?,
    val lon: Double?,
    var isFavorite: Boolean = false
) {
    constructor(cityResponse: CityResponse, isFavorite: Boolean = false) : this(
        cityResponse.id,
        cityResponse.name,
        cityResponse.country,
        cityResponse.coord?.lat,
        cityResponse.coord?.lon,
        isFavorite
    )

    constructor(entity: CitiesListEntity, isFavorite: Boolean?) : this(
        entity.id,
        entity.name,
        entity.country,
        entity.lat,
        entity.lon,
        isFavorite ?: entity.isFavorite
    )
}
