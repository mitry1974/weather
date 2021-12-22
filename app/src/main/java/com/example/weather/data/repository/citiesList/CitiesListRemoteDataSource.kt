package com.example.weather.data.repository.citiesList

import com.example.weather.api.ApiInterface
import com.example.weather.api.BaseRemoteDataSource
import com.example.weather.api.Result
import com.example.weather.data.local.database.entity.CityNameResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class CitiesListRemoteDataSource @Inject constructor(private val service: ApiInterface) :
    BaseRemoteDataSource() {
    suspend fun loadCitiesListFile(): Result<ResponseBody> =
        getResult {
            service.getCitiesListFile()
        }

    suspend fun loadCityNamesByCoordinates(
        lat: Double,
        lon: Double
    ): Result<List<CityNameResponse>> =
        getResult {
            service.getCityNamesByCoordinates(lat, lon)
        }
}