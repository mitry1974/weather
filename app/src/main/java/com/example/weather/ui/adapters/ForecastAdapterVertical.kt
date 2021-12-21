package com.example.weather.ui.main.favorites.newVersion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.databinding.ItemCitiesListNewBinding
import com.example.weather.ui.adapters.OnItemClickCallback
import com.example.weather.ui.common.DiffCallBack
import com.example.weather.util.ImageLoader

class WeatherCitiesAdapterVertical(private val onItemClickCallback: OnItemClickCallback? = null) :
    RecyclerView.Adapter<WeatherCitiesAdapterVertical.WeatherViewHolder>() {
    private var mDiffer = AsyncListDiffer(this, DiffCallBack<CityWeatherEntity>())

    fun setDataList(dataList: List<CityWeatherEntity>) {
        mDiffer.submitList(dataList)
    }

    fun addDataList(dataList: List<CityWeatherEntity>) {
        mDiffer.currentList.addAll(dataList)
    }

    fun clearDataList() {
        mDiffer.currentList.clear()
    }

    override fun getItemCount(): Int = mDiffer.currentList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder =
        WeatherViewHolder(
            ItemCitiesListNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = mDiffer.currentList[position]
        holder.bind(item, onItemClickCallback)
    }

    class WeatherViewHolder(private val binding: ItemCitiesListNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cityWeather: CityWeatherEntity, onItemClickCallback: OnItemClickCallback?) {
            binding.cityWeatherRecyclerView.apply {

                val horizontalAdapter = WeatherItemsAdapterHorizontal()
                horizontalAdapter.setDataList(cityWeather.propertiesList)

                adapter = horizontalAdapter
                layoutManager = LinearLayoutManager(
                    binding.cityWeatherRecyclerView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            binding.cityItemFavoriteImageView.setImageResource(R.drawable.ic_baseline_favorite)

            ImageLoader.loadImage(binding.cityWeatherImageView, cityWeather.iconFileName ?: "")
            binding.weatherName.text = cityWeather.weatherName
            binding.caption.text = cityWeather.fullCityName

            onItemClickCallback?.let {
                itemView.setOnClickListener {
                    cityWeather.cityId?.let {
                        onItemClickCallback.onItemClick(cityWeather.name ?: "", it)
                    }

                }

                binding.cityItemFavoriteImageView.setOnClickListener {
                    cityWeather.cityId?.let {
                        onItemClickCallback.onFavoriteClick(it)
                    }
                }
            }
        }
    }

}