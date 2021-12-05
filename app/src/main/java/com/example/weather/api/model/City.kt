package com.example.weather.api.model

import com.google.gson.annotations.SerializedName


data class CityDto(

    @SerializedName("id")
    val cityId: Int,

    val name: String,

    val country: String,

    @SerializedName("coord")
    val coordinates: Coordinates,
) {
    data class Coordinates(val lat: Double, val lon: Double)
}