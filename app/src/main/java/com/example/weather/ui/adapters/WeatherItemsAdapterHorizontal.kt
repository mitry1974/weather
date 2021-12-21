package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data.local.database.entity.BaseWeatherInfo
import com.example.weather.databinding.ItemWeatherElementListBinding
import com.example.weather.ui.common.DiffCallBack

class WeatherItemsAdapterHorizontal:
    RecyclerView.Adapter<WeatherItemsAdapterHorizontal.WeatherItemsViewHolder>() {
    private var mDiffer = AsyncListDiffer(this, DiffCallBack<BaseWeatherInfo.WeatherItem>())

    fun setDataList(dataList: List<BaseWeatherInfo.WeatherItem?>) {
        mDiffer.submitList(dataList)
    }

    class WeatherItemsViewHolder(private val binding: ItemWeatherElementListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: BaseWeatherInfo.WeatherItem) {
            binding.weatherItemCaption.text = itemData.key
            binding.weatherItemValue.text = itemData.value
            binding.weatherElementImage.setImageResource(itemData.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemsViewHolder =
        WeatherItemsViewHolder(ItemWeatherElementListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))


    override fun onBindViewHolder(holder: WeatherItemsViewHolder, position: Int) {
        holder.bind( mDiffer.currentList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size
}