package com.example.weather.ui.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DiffCallBack<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem === newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

}