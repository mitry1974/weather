package com.example.weather.data.repository.forecast

import com.example.weather.api.Result
import com.example.weather.api.model.CityForecastResponse
import com.example.weather.api.successed
import com.example.weather.data.local.database.CityForecastEntity
import com.example.weather.data.local.database.ForecastRow
import com.example.weather.data.repository.citiesList.CitiesListLocalDataSource
import com.example.weather.data.repository.citiesWeather.WeatherRemoteDataSource
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ForecastRepository @Inject constructor(
    private val remoteCityWeatherDataSource: WeatherRemoteDataSource,
    private val localCityForecastDataSource: ForecastLocalDataSource,
    private val citiesListLocalDataSource: CitiesListLocalDataSource,
) {
    suspend fun insertCityForecast(cityForecast: CityForecastEntity) {
        val data = localCityForecastDataSource.insertCityForecast(cityForecast)
        Result.Success(data)
    }

    private fun forecastResponseToEntity(
        cityId: Int,
        response: CityForecastResponse
    ): CityForecastEntity {
        val forecastRows = ArrayList<ForecastRow>()
        response.daily?.forEach { item ->
            val forecastDate = item.dt?.let {
                SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(Date(item.dt * 1000))
            }
            forecastRows.add(
                ForecastRow(
                    main = item.weather?.first()?.main,
                    icon = item.weather?.first()?.icon,
                    date = forecastDate
//                    temp = item.main?.temp,
//                    humidity = item.
                )
            )
        }

        return CityForecastEntity(
            id = cityId,
//            temp = response.list?.first()?.main?.temp,
//            icon = response.list?.first()?.weather?.first()?.icon,
//            time = response.list?.first()?.dtTxt,
            forecastRows = forecastRows
        )
    }

    suspend fun getCityForecast(id: Int): CityForecastEntity {
        val cityCoordinates = citiesListLocalDataSource.getCityCoordinatesById(id)
        val result = remoteCityWeatherDataSource.loadCityForecastByLatLon(
            cityCoordinates.lat, cityCoordinates.lon
        )
        return when {
            result is Result.Success<*> && result.successed -> {
                localCityForecastDataSource.deleteForecastByCityId(id)
                val entity = forecastResponseToEntity(id, (result as Result.Success).data)
                localCityForecastDataSource.insertCityForecast(entity)
                entity
            }
            else -> {
                if (result is Result.Error) {
                    println(result.message)
                }
                return localCityForecastDataSource.getCityForecastById(id)
            }
        }
    }


}