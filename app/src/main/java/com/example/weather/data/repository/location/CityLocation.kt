package com.example.weather.data.repository.location

import android.location.Location

data class CityLocation(val lat: Double, val lon: Double) {
    constructor(location: Location) : this(location.latitude, location.longitude)
    constructor() : this(0.0, 0.0)
}
