package com.example.weather.ui.main.favorites

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.api.Result
import com.example.weather.api.successed

import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.data.repository.citiesList.CitiesListRepository
import com.example.weather.data.repository.citiesWeather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val citiesListRepository: CitiesListRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    val favoriteCitiesWeather = MutableLiveData<List<CityWeatherEntity>>()

    private val _cityWeatherOnChange = MutableLiveData<CityWeatherEntity>()
    val cityWeatherOnChange: LiveData<CityWeatherEntity> = _cityWeatherOnChange

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    fun getFavoritesWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val citiesWeather = weatherRepository.getFavoriteCitiesWeather()) {
                is Result.Success -> {
                    if (citiesWeather.successed) {
                        citiesWeather.data.let {
                            favoriteCitiesWeather.postValue(it)
                        }
                    }
                }
            }

        }
    }

    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            citiesListRepository.updateFavoriteStatus(id)
            weatherRepository.deleteCityWeather(id)
            getFavoritesWeather()
        }
    }

}
//    private val citiesListRepository: CitiesListRepository,
//    private val favoritesRepository: FavoritesRepository): ViewModel() {
//
//    private val _toastError = MutableLiveData<String>()
//    val toastError: LiveData<String> = _toastError
//
//    private val _cityOnChange = MutableLiveData<CitiesListEntity>()
//    val cityOnChange = _cityOnChange
//
//    fun updateFavoriteStatus(id: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            when (val result = citiesListRepository.updateFavoriteStatus(id)) {
//                is Result.Success -> _cityOnChange.postValue(result.data!!)
//                is Result.Error -> _toastError.postValue(result.message)
//            }
//        }
//    }
//
//    val favoriteCities = favoritesRepository.favoriteCities
