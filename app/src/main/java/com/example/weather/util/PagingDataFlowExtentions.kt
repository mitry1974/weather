package com.example.weather.util

import androidx.paging.PagingData
import androidx.paging.filter
import com.example.weather.data.local.database.entity.CitiesListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<PagingData<CitiesListEntity>>.filterByCityName(query: String) =
    map { pagingData ->
        pagingData.filter { city ->
            city.name?.contains(query) ?: false
        }
    }
