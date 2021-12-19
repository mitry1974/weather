package com.example.weather.data.repository.citiesWeather

import com.example.weather.api.Result
import com.example.weather.api.successed
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.data.repository.citiesList.CitiesListLocalDataSource
import com.example.weather.util.Constants
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
                val iconFileName = getCityWeatherIcon(cityResponse.weather?.get(0)?.icon)
                CityWeatherEntity(cityResponse, iconFileName)
            }


    private suspend fun getCityWeatherIcon(icon: String?): String? {
        if(icon.isNullOrBlank()) return ""

        var iconFileName = iconsStorage.getIconFileName(icon)
        if (iconFileName.isNullOrBlank()) {
            when (val result = remoteCityWeatherDataSource.loadCityWeatherIcon(icon)) {
                is Result.Success -> {
                    if (result.successed) {
                        iconsStorage.saveIcon(icon, result.data.bytes())
                        iconFileName = iconsStorage.getIconFileName(icon)
                    } else {
                        Result.Error(Constants.GENERIC_ERROR)
                    }
                }
                else -> result as Result.Error
            }
        }
        return iconFileName
    }

    private fun isNeedLoadData(): Boolean {
        return true
//        val lastLoadedTime = preferenceStorage.timeLoadedAt
//        val currentTime = Date().time
//        // 2 hours
//        return currentTime - lastLoadedTime > 7200 * 1000
    }

}