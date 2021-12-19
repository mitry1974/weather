package com.example.weather.data.repository.citiesList

import com.example.weather.api.Result
import com.example.weather.api.Result.Success
import com.example.weather.api.model.CityNameResponse
import com.example.weather.api.model.CityResponse
import com.example.weather.api.successed
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.InputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.zip.GZIPInputStream
import javax.inject.Inject

class CitiesListRepository @Inject constructor(
    private val citiesListRemoteDataSource: CitiesListRemoteDataSource,
    private val citiesListLocalDataSource: CitiesListLocalDataSource,
    private val preferenceStorage: PreferenceStorage
) {
    private fun getCitiesFromZipStream(data: InputStream): Array<CityResponse> {
        GZIPInputStream(data).bufferedReader(UTF_8).use {
            return Gson().fromJson(it, Array<CityResponse>::class.java)
        }
    }

    fun getPagingDataSource(query: String) =
        citiesListLocalDataSource.getCitiesListPagedFiltered(query)

    suspend fun loadCitiesList() {
        when (val result = citiesListRemoteDataSource.loadCitiesListFile()) {
            is Success -> {
                if (result.successed) {
                    val cities = withContext(Dispatchers.IO) {
                        getCitiesFromZipStream(result.data.byteStream())
                    }
                    val favoriteIds = withContext(Dispatchers.IO) {
                        citiesListLocalDataSource.favoriteIds()
                    }
                    val customCitiesList = cities
                        .filter { item -> item.id != 0 }
                        .map { city ->
                            CitiesListEntity(
                                city,
                                favoriteIds.contains(city.id)
                            )
                        }

                    citiesListLocalDataSource.insertCitiesIntoDatabase(customCitiesList)
                    Success(true)
                } else {
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            else -> result as Result.Error
        }
    }

    suspend fun getCityNameByCoordinates(lat: Double, lon: Double): Result<String> {
        val result = citiesListRemoteDataSource.loadCityNamesByCoordinates(lat, lon)
        return when {
            result is Result.Error -> result
            result is Success<*> && !result.successed -> Result.Error(Constants.GENERIC_ERROR)
            else -> {
                val citiesNames: List<CityNameResponse> = (result as Success).data
                val name = if (citiesNames.size == 1) citiesNames[0].name else ""
                Success(name)
            }
        }
    }

    suspend fun updateFavoriteStatus(id: Int): Result<CitiesListEntity> {
        val result = citiesListLocalDataSource.updateFavoriteStatus(id)
        return result?.let {
            Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    fun isFirstRun(): Boolean {
        val result = preferenceStorage.isFirstRun
        preferenceStorage.isFirstRun = false
        return result
    }

}