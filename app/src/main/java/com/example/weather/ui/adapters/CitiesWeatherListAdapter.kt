package com.example.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.local.database.CitiesListEntity


class CitiesWeatherListAdapter : RecyclerView.Adapter<CitiesWeatherListAdapter.CitiesWeatherListViewHolder>() {
    lateinit var items: List<CitiesListEntity>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesWeatherListViewHolder =
        CitiesWeatherListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cities_weather_list, parent, false)
        )


    override fun onBindViewHolder(holder: CitiesWeatherListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class CitiesWeatherListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CitiesListEntity) {

        }
    }
}