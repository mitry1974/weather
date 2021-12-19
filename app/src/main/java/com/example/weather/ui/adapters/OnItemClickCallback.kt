package com.example.weather.ui.adapters

interface OnItemClickCallback {
    fun onFavoriteClick(cityID: Int)
    fun onItemClick(cityName: String, cityID: Int)
}
