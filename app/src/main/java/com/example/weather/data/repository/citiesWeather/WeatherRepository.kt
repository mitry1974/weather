package com.example.weather.data.repository.citiesWeather

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.example.weather.R
import com.example.weather.api.Result
import com.example.weather.api.successed
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.data.local.iconsStorage.WeatherIconsStorage
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.data.repository.citiesList.CitiesListLocalDataSource
import com.example.weather.errors.WeatherException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val localCityWeatherDataSource: WeatherLocalDataSource,
    private val remoteCityWeatherDataSource: WeatherRemoteDataSource,
    private val citiesListLocalDataSource: CitiesListLocalDataSource,
    private val iconsStorage: WeatherIconsStorage,
    private val preferenceStorage: PreferenceStorage
) {
    val favoriteCitiesWeather = MutableLiveData<List<CityWeatherEntity>>()

    suspend fun getFavoriteCitiesWeather() {
        val favoriteCitiesIds = citiesListLocalDataSource.favoriteIds()
        if(favoriteCitiesIds.isEmpty()) {
            favoriteCitiesWeather.postValue(listOf())
            return
        }

        var citiesWeather = loadCitiesWeather(favoriteCitiesIds)
        if(citiesWeather.isNotEmpty()) {
            localCityWeatherDataSource.insertCitiesWeather(citiesWeather)
        } else {
            citiesWeather = localCityWeatherDataSource.getCitiesWeather(
                favoriteCitiesIds
            )
        }

        if(citiesWeather.isEmpty()) {
            throw WeatherException(R.string.error_load_weather)
        }

        citiesWeather.let{
            favoriteCitiesWeather.postValue(it)
        }
    }

    suspend fun deleteCityWeather(cityId: Int) =
        localCityWeatherDataSource.deleteCityWeather(cityId)

    private suspend fun loadCitiesWeather(favoriteCitiesIds: List<Int>): List<CityWeatherEntity> {
        return favoriteCitiesIds
            .map { remoteCityWeatherDataSource.loadCityWeatherById(it) }
            .filter {it is Result.Success && it.successed}
            .map {
                val cityResponse = (it as Result.Success).data
                val iconFileName =
                    iconsStorage.getWeatherIconFileName(cityResponse.weather?.get(0)?.icon)
                CityWeatherEntity(cityResponse, iconFileName)
            }
    }
}