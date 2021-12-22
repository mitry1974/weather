package com.example.weather.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    fun postError(message: String?) {
        message?.let {
            _toastError.postValue(it)
        }
    }

    fun postError(id: Int) =
        postError(getResourceString(id))

    private fun getResourceString(id: Int): String? {
        return getApplication<Application>().resources.getString(id)
    }
}