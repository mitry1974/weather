package com.example.weather.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CitiesListDao {
    @Query("SELECT * FROM cities_list")
    fun citiesList(): LiveData<List<CitiesListEntity>>

    @Query("SELECT * FROM cities_list WHERE id = :id")
    fun cityFromId(id: Int): CitiesListEntity

    @Query("SELECT id FROM cities_list WHERE isFavorite = 1")
    fun favoriteIds(): List<Int>

    @Query("SELECT * FROM cities_list WHERE isFavorite = 1")
    fun favoriteCities(): LiveData<List<CitiesListEntity>>

    @Update
    fun updateCitiesListEntity(data: CitiesListEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CitiesListEntity>)
}