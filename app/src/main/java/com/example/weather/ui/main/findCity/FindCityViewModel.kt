package com.example.weather.ui.main.findCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.api.Result
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.repository.citiesList.CitiesListRepository
import com.example.weather.data.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindCityViewModel @Inject constructor(
    private val citiesListRepository: CitiesListRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    private val _cityOnChange = MutableLiveData<CitiesListEntity?>()
    val cityOnChange: LiveData<CitiesListEntity?> = _cityOnChange

    private val _homeCityName = MutableLiveData<String?>()
    val homeCityName: LiveData<String?> = _homeCityName

    val location = locationRepository.currentLocation

    fun loadCities() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            citiesListRepository.loadCitiesList()
            _isLoading.postValue(false)
        }
    }

    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = citiesListRepository.updateFavoriteStatus(id)
            when {
                result is Result.Success -> _cityOnChange.postValue(result.data)
                result is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun getLocationByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = citiesListRepository.getCityNameByCoordinates(lat, lon)
            when {
                result is Result.Success -> _homeCityName.postValue(result.data)
                result is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun getLocation() {
        locationRepository.getLocation()
    }
}