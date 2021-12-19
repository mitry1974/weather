package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.ui.common.DiffCallBack
import com.example.weather.data.local.database.entity.CityWeatherEntity
import com.example.weather.databinding.ItemCitiesWeatherListBinding
import com.example.weather.util.*


class CitiesWeatherListAdapter(private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<CitiesWeatherListAdapter.CitiesWeatherListViewHolder>() {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CitiesWeatherListViewHolder =
        CitiesWeatherListViewHolder(
            ItemCitiesWeatherListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: CitiesWeatherListViewHolder, position: Int) {
        val item = mDiffer.currentList[position]
        holder.bind(item, onItemClickCallback)
    }

    override fun getItemCount(): Int = mDiffer.currentList.size


    class CitiesWeatherListViewHolder(private val binding: ItemCitiesWeatherListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cityWeather: CityWeatherEntity, onItemClickCallback: OnItemClickCallback) {
            binding.cityNameItemTextView.text = cityWeather.name
            binding.cityTemperatureItemTextView.text = cityWeather.temp?.celsius()
            binding.cityWeatherPressure.text = cityWeather.pressure?.hpaInHg()
            binding.cityWeatherWind.text = cityWeather.wind?.speed()
            binding.cityWeatherHumidity.text = cityWeather.humidity?.percent()
            binding.cityItemFavoriteImageView.setImageResource(R.drawable.ic_baseline_favorite)
            binding.cityWeatherDescription.text = cityWeather.weatherName

            binding.cityItemFavoriteImageView.setOnClickListener {
                cityWeather.cityId?.let {
                    onItemClickCallback.onFavoriteClick(it)
                }
            }

            ImageLoader.loadImage(binding.cityWeatherImageView, cityWeather.iconFileName ?: "")

            itemView.setOnClickListener {
                cityWeather.cityId?.let {
                    onItemClickCallback.onItemClick(cityWeather.name ?: "", it)
                }
            }
        }
    }
}