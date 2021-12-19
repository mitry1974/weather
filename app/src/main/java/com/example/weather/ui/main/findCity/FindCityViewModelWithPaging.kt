package com.example.weather.ui.main.findCity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.weather.api.Result
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.data.repository.citiesList.CitiesListRepository
import com.example.weather.data.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindCityViewModelWithPaging @Inject constructor(
    private val citiesListRepository: CitiesListRepository,
    private val locationRepository: LocationRepository,

    ) : ViewModel() {
    var query: String = ""

    fun getAllCities() =
        Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 2, maxSize = 200)) {
            citiesListRepository.getPagingDataSource(this.query)
        }.flow.cachedIn(viewModelScope)


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGPSGetting = MutableLiveData<Boolean>()
    val isGPSGetting: LiveData<Boolean> = _isGPSGetting

    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    private val _cityOnChange = MutableLiveData<CitiesListEntity>()
    val cityOnChange: LiveData<CitiesListEntity> = _cityOnChange

    private val _homeCityName = MutableLiveData<String>()
    val homeCityName: LiveData<String> = _homeCityName

    val location = locationRepository.currentLocation

    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = citiesListRepository.updateFavoriteStatus(id)) {
                is Result.Success -> _cityOnChange.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun getLocationByCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = citiesListRepository.getCityNameByCoordinates(lat, lon)) {
                is Result.Success -> _homeCityName.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun getLocation() {
        _isGPSGetting.postValue(true)
        locationRepository.getLocation()
        _isGPSGetting.postValue(false)
    }

    fun checkFirstRunAndLoadData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (citiesListRepository.isFirstRun()) {
                _isLoading.postValue(true)
                citiesListRepository.loadCitiesList()
                _isLoading.postValue(false)
            }
        }
    }
}
