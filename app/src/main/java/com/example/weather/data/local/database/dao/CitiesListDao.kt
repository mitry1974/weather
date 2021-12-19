package com.example.weather.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.weather.data.local.database.entity.CitiesListEntity

@Dao
interface CitiesListDao {
    @Query("SELECT * FROM cities_list WHERE name LIKE :query")
    fun citiesListPagedFiltered(query: String): PagingSource<Int, CitiesListEntity>

    @Query("SELECT * FROM cities_list")
    fun citiesList(): LiveData<List<CitiesListEntity>>

    @Query("SELECT * FROM cities_list WHERE id = :id")
    fun cityById(id: Int): CitiesListEntity

    @Query("SELECT id FROM cities_list WHERE isFavorite = 1")
    fun favoriteIds(): List<Int>

    @Query("SELECT * FROM cities_list WHERE isFavorite = 1")
    fun favoriteCities(): LiveData<List<CitiesListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(list: List<CitiesListEntity>): List<Long>

    @Update
    suspend fun updateCitiesListEntity(data: CitiesListEntity): Int
}