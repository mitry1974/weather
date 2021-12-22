package com.example.weather.ui.main.forecast

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.local.database.CityForecastEntity
import com.example.weather.data.repository.forecast.ForecastRepository
import com.example.weather.errors.WeatherException
import com.example.weather.ui.common.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    application: Application,
): WeatherViewModel(application)  {
    private var cityId: Int? = 0

    var forecastData = MutableLiveData<CityForecastEntity>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun getForecast() {
        cityId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    _isLoading.postValue(true)
                    val forecast = forecastRepository.getCityForecast(cityId!!)
                    forecastData.postValue(forecast)
                } catch (e: WeatherException) {
                    postError(e.messageID)
                } catch (e: Exception) {
                    postError(e.message)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun setForecastInfo(cityId: Int?) {
        this.cityId = cityId
    }
}