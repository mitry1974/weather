package com.example.weather.data.repository.citiesList

import com.example.weather.data.local.database.CitiesListEntity
import com.example.weather.data.local.preferences.PreferenceStorage
import com.example.weather.api.successed
import com.example.weather.api.Result
import com.example.weather.util.Constants
import java.util.*
import javax.inject.Inject

class CitiesListRepository @Inject constructor(
    private val citiesListRemoteDataSource: CitiesListRemoteDataSource,
    private val citiesListLocalDataSource: CitiesListLocalDataSource,
    private val preferenceStorage: PreferenceStorage
) {
    val allCitiesList = citiesListLocalDataSource.allCitiesList

    suspend fun citiesList() {
        //Запрашиваем данные у удаленного источника данных и выполняем проверку результата
        when (val result = citiesListRemoteDataSource.citiesList()) {
            //выполняем если запрос выполнен успешно
            is Result.Success -> {
                //проверяем есть ли в результате данные
                if (result.successed) {
                    //получаем список сокращенных имен для избранных криптовалют
                    //в дальнейшем с его помощью будем устанавливать
                    //флаг в нужное положение
                    val favoriteIds = citiesListLocalDataSource.favoriteIds()

                    //создаем новый список в который положим обработанные данные
                    val customCityList = result.data.let {
                        it.filter { item -> item.id != 0 }
                            .map { city ->
                                CitiesListEntity(
                                    city.id,
                                    city.name,
                                    city.lat,
                                    city.lon,
                                    favoriteIds.contains(city.id)
                                )
                            }
                    }

                    //записываем данные в базу данных
                    citiesListLocalDataSource.insertCitiesIntoDatabase(customCityList)

                    //обновляем время последнего обновления данных
                    preferenceStorage.timeLoadedAt = Date().time

                    //возвращаем результат
                    Result.Success(true)
                } else {
                    //в остальных случаях возвращаем ошибку
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            //в остальных случаях возвращаем ошибку
            else -> result as Result.Error
        }
    }

    suspend fun updateFavoriteStatus(id: Int): Result<CitiesListEntity> {
        val result = citiesListLocalDataSource.updateFavoriteStatus(id)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    fun loadData(): Boolean {
        //получаем время последней загрузки данных
        val lastLoadedTime = preferenceStorage.timeLoadedAt
        //получаем текущее время
        val currentTime = Date().time
        //проверем прошло ли 20 секунд (20 * 1000
        //где 20 - это количество секунд,
        //а 1000 это количество миллисекунд в одной секунде)
        return currentTime - lastLoadedTime > 20 * 1000
    }
}