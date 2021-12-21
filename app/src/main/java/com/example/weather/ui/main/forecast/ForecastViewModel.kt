package com.example.weather.ui.main.forecast

import androidx.lifecycle.*
import com.example.weather.data.local.database.CityForecastEntity
import com.example.weather.data.repository.forecast.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository

) : ViewModel() {
    private var cityId: Int? = 0

    var forecastData = MutableLiveData<CityForecastEntity>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getForecast() {
        cityId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                val forecast = forecastRepository.getCityForecast(cityId!!)
                forecastData.postValue(forecast)
                _isLoading.postValue(false)
            }
        }
    }

    fun setForecastInfo(cityId: Int?) {
        this.cityId = cityId
    }
}