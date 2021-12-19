package com.example.weather.data.repository.location

import javax.inject.Inject

class LocationRepository @Inject constructor(private val locationDataSource: LocationDataSource) {
    val currentLocation = locationDataSource.location

    fun getLocation() {
        locationDataSource.getLocation()
    }
}