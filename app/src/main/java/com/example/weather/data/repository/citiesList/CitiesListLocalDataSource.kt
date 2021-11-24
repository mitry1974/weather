package com.example.weather.data.repository.citiesList

import androidx.lifecycle.LiveData
import com.example.weather.data.local.database.CitiesListEntity
import com.example.weather.data.local.database.CitiesWeatherDatabase
import javax.inject.Inject

class CitiesListLocalDataSource @Inject constructor(private val database: CitiesWeatherDatabase) {
    val allCitiesList: LiveData<List<CitiesListEntity>> = database.citiesListDao().citiesList()

    suspend fun insertCitiesIntoDatabase(citiesToInsert: List<CitiesListEntity>) {
        if (citiesToInsert.isNotEmpty()) {
            database.citiesListDao().insert(citiesToInsert)
        }
    }

    suspend fun favoriteIds(): List<Int> = database.citiesListDao().favoriteIds()

    suspend fun updateFavoriteStatus(id: Int): CitiesListEntity? {
        val city = database.citiesListDao().cityFromId(id)
        city?.let {
            val citiesListEntity = CitiesListEntity(
                it.id,
                it.name,
                it.lat,
                it.lon
            )

            if (database.citiesListDao().updateCitiesListEntity(citiesListEntity) > 0) {
                return citiesListEntity
            }
        }
        return null
    }
}