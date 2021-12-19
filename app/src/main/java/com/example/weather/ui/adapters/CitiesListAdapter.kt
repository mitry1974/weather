package com.example.weather.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.databinding.ItemCitiesListBinding

class CitiesListAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<CitiesListAdapter.CitiesListViewHolder>(),
    Filterable {
    private var items = mutableListOf<CitiesListEntity>()
    private var filteredItems = listOf<CitiesListEntity>()

    fun updateCitiesList(newList: List<CitiesListEntity>) {
        items.clear()
        items.addAll(newList)
        filteredItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder =
        CitiesListViewHolder(
            ItemCitiesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        Log.d("WEATHER_APP", "bind, position = " + position)
        holder.bind(filteredItems[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = filteredItems.size

    class CitiesListViewHolder(private val binding: ItemCitiesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: CitiesListEntity, onItemClickCallback: OnItemClickCallback) {
            binding.cityNameItemTextView.text = city.name
            binding.coordinatesLat.text = city.lat.toString()
            binding.coordinatesLon.text = city.lon.toString()

            binding.cityItemFavoriteImageView.setImageResource(
                if (city.isFavorite) R.drawable.ic_baseline_favorite
                else R.drawable.ic_baseline_favorite_border
            )

            binding.cityItemFavoriteImageView.setOnClickListener {

                onItemClickCallback.onFavoriteClick(city.id ?: 0)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredItems = if (charString.isEmpty()) {
                    items
                } else {
                    items.filter { it.name?.contains(charString) ?: false }
                }
                return FilterResults().apply { values = filteredItems }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredItems = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<CitiesListEntity>
                notifyDataSetChanged()
            }
        }
    }
}