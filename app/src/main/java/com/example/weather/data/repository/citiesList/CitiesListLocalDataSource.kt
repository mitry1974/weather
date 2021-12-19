package com.example.weather.data.repository.citiesList

import androidx.paging.PagingSource
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.local.database.CitiesWeatherDatabase
import com.example.weather.data.local.database.entity.Coordinates
import javax.inject.Inject

class CitiesListLocalDataSource @Inject constructor(private val database: CitiesWeatherDatabase) {
    fun getCitiesListPagedFiltered(query: String): PagingSource<Int, CitiesListEntity> =
        database.citiesListDao().citiesListPagedFiltered("%$query%")

    suspend fun insertCitiesIntoDatabase(citiesToInsert: List<CitiesListEntity>) {
        if (citiesToInsert.isNotEmpty()) {
            database.citiesListDao().insertCities(citiesToInsert)
        }
    }

    suspend fun favoriteIds(): List<Int> = database.citiesListDao().favoriteIds()

    suspend fun updateFavoriteStatus(id: Int): CitiesListEntity? {
        val city = database.citiesListDao().cityById(id)

        val updatedCity = CitiesListEntity(city, city.isFavorite.not())

        return if (database.citiesListDao()
                .updateCitiesListEntity(updatedCity) > 0
        ) updatedCity else null
    }

    suspend fun getCityById(cityId: Int): CitiesListEntity =
        database.citiesListDao().cityById(cityId)

    fun getCityCoordinatesById(cityId: Int): Coordinates {
        val entity = database.citiesListDao().cityById(cityId)
        return Coordinates(entity.lat ?: 0.0, entity.lon ?: 0.0)
    }
}