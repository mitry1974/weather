package com.example.weather.data.repository.citiesWeather

import com.example.weather.api.Result
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.data.local.iconsStorage.WeatherIconsStorage
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.data.repository.citiesList.CitiesListLocalDataSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val localCityWeatherDataSource: WeatherLocalDataSource,
    private val remoteCityWeatherDataSource: WeatherRemoteDataSource,
    private val citiesListLocalDataSource: CitiesListLocalDataSource,
    private val iconsStorage: WeatherIconsStorage,
    private val preferenceStorage: PreferenceStorage
) {
    suspend fun getFavoriteCitiesWeather(): Result<List<CityWeatherEntity>> {
        val favoriteCitiesIds = citiesListLocalDataSource.favoriteIds()
        if (isNeedLoadData()) {
            val citiesWeather = loadCitiesWeather(favoriteCitiesIds)
            localCityWeatherDataSource.insertCitiesWeather(citiesWeather)
        }

        return Result.Success(localCityWeatherDataSource.getCitiesWeather(favoriteCitiesIds))
    }

    suspend fun deleteCityWeather(cityId: Int) =
        localCityWeatherDataSource.deleteCityWeather(cityId)

    private suspend fun loadCitiesWeather(favoriteCitiesIds: List<Int>): List<CityWeatherEntity> =
        favoriteCitiesIds
            .map { remoteCityWeatherDataSource.loadCityWeatherById(it) }
            .map {
                val cityResponse = (it as Result.Success).data
                val iconFileName =
                    iconsStorage.getWeatherIconFileName(cityResponse.weather?.get(0)?.icon)
                CityWeatherEntity(cityResponse, iconFileName)
            }


    private fun isNeedLoadData(): Boolean {
        return true
//        val lastLoadedTime = preferenceStorage.timeLoadedAt
//        val currentTime = Date().time
//        // 2 hours
//        return currentTime - lastLoadedTime > 7200 * 1000
    }

}