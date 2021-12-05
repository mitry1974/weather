package com.example.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.api.model.City
import com.example.weather.data.local.database.CitiesListEntity
import com.example.weather.databinding.ItemCitiesListBinding

class CitiesListAdapter: RecyclerView.Adapter<CitiesListAdapter.CitiesListViewHolder>(){
    private var items = mutableListOf<CitiesListEntity>()

    fun updateCitiesList(newList: List<CitiesListEntity>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder =
        CitiesListViewHolder(
            ItemCitiesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class CitiesListViewHolder(private val binding: ItemCitiesListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(city: CitiesListEntity) {
            binding.cityNameItemTextView.text = city.name
            binding.coordinatesLat.text = city.lat.toString()
            binding.coordinatesLon.text = city.lon.toString()
        }
    }
}