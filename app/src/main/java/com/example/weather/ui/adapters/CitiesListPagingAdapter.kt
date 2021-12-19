package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.local.database.entity.CitiesListEntity
import com.example.weather.databinding.ItemCitiesListBinding

class CitiesListPagingAdapter(private val onItemClickCallback: OnItemClickCallback) :
    PagingDataAdapter<CitiesListEntity, CitiesListPagingAdapter.CitiesListPagedViewHolder>(
        CityListEntityComparator
    ) {
    inner class CitiesListPagedViewHolder(private val binding: ItemCitiesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: CitiesListEntity, onItemClickCallback: OnItemClickCallback) {
            binding.cityNameItemTextView.text = city.name
            binding.cityCountryTextView.text = city.country
            binding.coordinatesLat.text = city.lat.toString()
            binding.coordinatesLon.text = city.lon.toString()

            binding.cityItemFavoriteImageView.setImageResource(
                if (city.isFavorite) R.drawable.ic_baseline_favorite
                else R.drawable.ic_baseline_favorite_border
            )

            binding.cityItemFavoriteImageView.setOnClickListener {
                city.id?.let {
                    onItemClickCallback.onFavoriteClick(city.id)
                }
            }
        }
    }

    object CityListEntityComparator : DiffUtil.ItemCallback<CitiesListEntity>() {
        override fun areItemsTheSame(oldItem: CitiesListEntity, newItem: CitiesListEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CitiesListEntity, newItem: CitiesListEntity) =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: CitiesListPagedViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClickCallback) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListPagedViewHolder =
        CitiesListPagedViewHolder(
            ItemCitiesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
}