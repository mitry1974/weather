package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.local.database.CityForecastEntity
import com.example.weather.data.local.database.ForecastRow
import com.example.weather.databinding.ItemCitiesWeatherListBinding
import com.example.weather.databinding.ItemForecastListBinding
import com.example.weather.ui.common.DiffCallBack
import com.example.weather.util.*

class ForecastListAdapter :
    RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder>() {
    private var mDiffer = AsyncListDiffer(this, DiffCallBack<ForecastRow>())

    fun setDataList(data: CityForecastEntity) {
        mDiffer.submitList(data.forecastRows)
    }

    fun addDataList(dataList: List<ForecastRow>) {
        mDiffer.currentList.addAll(dataList)
    }

    fun clearDataList() {
        mDiffer.currentList.clear()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastListViewHolder =
        ForecastListViewHolder(
            ItemForecastListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ForecastListViewHolder, position: Int) {
        val item = mDiffer.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mDiffer.currentList.size


    class ForecastListViewHolder(private val binding: ItemForecastListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastRow: ForecastRow) {            binding.dateTextView.text = forecastRow.date
            binding.cityTemperatureItemTextView.text = forecastRow.temp?.celsius()
            binding.cityWeatherPressure.text = forecastRow.pressure?.hpaInHg()
            binding.cityWeatherWind.text = forecastRow.wind?.speed()
            binding.cityWeatherHumidity.text = forecastRow.humidity?.percent()
            binding.cityItemFavoriteImageView.setImageResource(R.drawable.ic_baseline_favorite)
            binding.cityWeatherDescription.text = forecastRow.main
//            ImageLoader.loadImage(binding.cityWeatherImageView, forecastRow.iconFileName ?: "")
        }
    }
}