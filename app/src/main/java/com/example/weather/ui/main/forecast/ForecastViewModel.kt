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
    private var forecastDays: Int? = 0
    private var cityId: Int? = 0
    private val _index = MutableLiveData<Int>()
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

    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it\n forecast for city with id: $cityId for $forecastDays days"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun setForecastInfo(cityId: Int?, forecastDays: Int?) {
        this.cityId = cityId
        this.forecastDays = forecastDays
    }
}