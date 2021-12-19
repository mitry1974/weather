package com.example.weather.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.weather.R

object ImageLoader {
    fun loadImage(view: ImageView, url: String, placeholder: Int = R.drawable.ic_baseline_weather) {
        Glide.with(view) //передаем ImageView или Context
            .load(url) //передаем ссылку на изобрежение
            .placeholder(placeholder) //указываем картинку по умолчанию
            .error(placeholder) //указываем картинку на случай какой-то ошибки
            .fallback(placeholder) //указываем картинку на случай если сервер не отдаст картинку
            .into(view) //передаем ImageView в который хотим загрузить изображение
    }
}