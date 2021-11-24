package com.example.weather.data.repository.citiesList

import com.example.weather.api.ApiInterface
import com.example.weather.api.BaseRemoteDataSource
import com.example.weather.api.model.City
import com.example.weather.api.Result
import javax.inject.Inject

class CitiesListRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {
    suspend fun citiesList(): Result<List<City>> =
        getResult {
            service.citiesList()
        }

}