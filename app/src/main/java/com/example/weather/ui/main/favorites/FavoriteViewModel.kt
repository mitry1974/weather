package com.example.weather.ui.main.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.data.repository.citiesList.CitiesListRepository
import com.example.weather.data.repository.citiesWeather.WeatherRepository
import com.example.weather.errors.WeatherException
import com.example.weather.ui.common.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val citiesListRepository: CitiesListRepository,
    private val weatherRepository: WeatherRepository,
    application: Application,
) : WeatherViewModel(application) {
    val favoriteCitiesWeather = weatherRepository.favoriteCitiesWeather

    private val _cityWeatherOnChange = MutableLiveData<CityWeatherEntity>()
    val cityWeatherOnChange: LiveData<CityWeatherEntity> = _cityWeatherOnChange

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFavoritesWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getFavoriteCitiesWeather()
            } catch (e: WeatherException) {
                postError(e.messageID)
            } catch (e: java.lang.Exception) {
                postError(e.message)
            }
        }
    }


    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                citiesListRepository.updateFavoriteStatus(id)
                weatherRepository.deleteCityWeather(id)
                getFavoritesWeather()
            } catch (e: WeatherException) {
                postError(e.messageID)
            } catch (e: java.lang.Exception) {
                postError(e.message)
            }
        }
    }

}
