package com.example.weather.data.repository.citiesList

import android.content.res.Resources
import com.example.weather.R
import com.example.weather.api.Result
import com.example.weather.api.model.CityResponse
import com.example.weather.api.successed
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.local.database.entity.CityNameResponse
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.errors.WeatherException
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
        val result = citiesListRemoteDataSource.loadCitiesListFile()
        when {
            result is Result.Success && result.successed -> {
                val cities = withContext(Dispatchers.IO) {
                    getCitiesFromZipStream(result.data.byteStream())
                }
                val favoriteIds = withContext(Dispatchers.IO) {
                    citiesListLocalDataSource.favoriteIds()
                }
                val customCitiesList = cities
                    .filter { item -> item.id != 0 }
                    .map { city ->
                        println(city)
                        CitiesListEntity(
                            city,
                            favoriteIds.contains(city.id)
                        )
                    }

                citiesListLocalDataSource.insertCitiesIntoDatabase(customCitiesList)
            }
            else -> {
                throw WeatherException(R.string.error_load_cities_list)
            }
        }
    }

    suspend fun getCityNameByCoordinates(lat: Double, lon: Double): String {
        val result = citiesListRemoteDataSource.loadCityNamesByCoordinates(lat, lon)
        return when {
            result is Result.Success<*> && result.successed -> {
                val citiesNames: List<CityNameResponse> = (result as Result.Success).data
                val name = if (citiesNames.size == 1) citiesNames[0].name else ""
                name
            }
            else -> throw WeatherException(R.string.error_find_location_by_GPS)
        }
    }

    suspend fun updateFavoriteStatus(id: Int): CitiesListEntity =
        citiesListLocalDataSource.updateFavoriteStatus(id)
            ?: throw WeatherException(R.string.error_city_status_changing)

    fun isFirstRun(): Boolean {
        return preferenceStorage.isFirstRun
    }

    fun setFirstRun(value: Boolean) = run { preferenceStorage.isFirstRun = value }
}