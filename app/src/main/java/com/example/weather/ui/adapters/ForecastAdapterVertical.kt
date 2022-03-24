package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.local.database.entity.ForecastRow
import com.example.weather.databinding.ItemCitiesListNewBinding
import com.example.weather.ui.common.DiffCallBack
import com.example.weather.util.ImageLoader

class ForecastAdapterVertical :
    RecyclerView.Adapter<ForecastAdapterVertical.WeatherViewHolder>() {
    private var mDiffer = AsyncListDiffer(this, DiffCallBack<ForecastRow>())

    fun setDataList(dataList: List<ForecastRow>) {
        mDiffer.submitList(dataList)
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
        holder.bind(item)
    }

    class WeatherViewHolder(private val binding: ItemCitiesListNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastRow: ForecastRow) {
            binding.cityWeatherRecyclerView.apply {

                val horizontalAdapter = WeatherItemsAdapterHorizontal()
                horizontalAdapter.setDataList(forecastRow.propertiesList)

                adapter = horizontalAdapter
                layoutManager = LinearLayoutManager(
                    binding.cityWeatherRecyclerView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            binding.cityItemFavoriteImageView.visibility = GONE

            ImageLoader.loadImage(binding.cityWeatherImageView, forecastRow.iconFileName ?: "")
            binding.weatherName.text = forecastRow.weatherName
            binding.caption.text = forecastRow.date
        }
    }
}