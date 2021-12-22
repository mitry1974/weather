package com.example.weather.ui.main.findCity

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.repository.citiesList.CitiesListRepository
import com.example.weather.data.repository.location.LocationRepository
import com.example.weather.errors.WeatherException
import com.example.weather.ui.common.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindCityViewModelWithPaging @Inject constructor(
    private val citiesListRepository: CitiesListRepository,
    private val locationRepository: LocationRepository,
    application: Application
) : WeatherViewModel(application) {
    var query: String = ""

    fun getAllCities() =
        Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 2, maxSize = 200)) {
            citiesListRepository.getPagingDataSource(this.query)
        }.flow.cachedIn(viewModelScope)


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGPSGetting = MutableLiveData<Boolean>()
    val isGPSGetting: LiveData<Boolean> = _isGPSGetting

    private val _cityOnChange = MutableLiveData<CitiesListEntity>()
    val cityOnChange: LiveData<CitiesListEntity> = _cityOnChange

    private val _homeCityName = MutableLiveData<String>()
    val homeCityName: LiveData<String> = _homeCityName

    val location = locationRepository.currentLocation

    @SuppressLint("NullSafeMutableLiveData")
    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val city = citiesListRepository.updateFavoriteStatus(id)
                _cityOnChange.postValue(city)
            } catch (e: WeatherException) {
                postError(e.messageID)
            } catch (e: java.lang.Exception) {
                postError(e.message)
            }
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getLocationByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cityName = citiesListRepository.getCityNameByCoordinates(lat, lon)
                println("2 - $cityName")
                _homeCityName.postValue(cityName)
                println("3")
            } catch (e: WeatherException) {
                postError(e.messageID)
            } catch (e: java.lang.Exception) {
                postError(e.message)
            }
        }
    }

    fun getLocation() {
        try {
            _isGPSGetting.postValue(true)
            locationRepository.getLocation()
            _isGPSGetting.postValue(false)
        } catch (e: WeatherException) {
            postError(e.messageID)
        } catch (e: java.lang.Exception) {
            postError(e.message)
        }
    }

    fun checkFirstRunAndLoadCitiesList() {
        if (citiesListRepository.isFirstRun()) {
            loadCitiesList()
        }
    }

    fun loadCitiesList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                citiesListRepository.loadCitiesList()
                citiesListRepository.setFirstRun(false)
            } catch (e: WeatherException) {
                postError(e.messageID)
            } catch (e: java.lang.Exception) {
                postError(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


}
