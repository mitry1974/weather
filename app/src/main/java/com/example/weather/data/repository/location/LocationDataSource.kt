package com.example.weather.data.repository.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationDataSource @Inject constructor(@ApplicationContext val context: Context) :
    LocationListener {
    var location = MutableLiveData<CityLocation>()

    private lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    fun getLocation() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        this.location.value = CityLocation(location)
    }
}