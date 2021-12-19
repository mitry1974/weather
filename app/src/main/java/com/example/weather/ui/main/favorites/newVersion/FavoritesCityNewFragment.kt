package com.example.weather.ui.main.favorites.newVersion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentFavoritesBinding
import com.example.weather.ui.adapters.OnItemClickCallback
import com.example.weather.ui.common.MainNavigationFragment
import com.example.weather.ui.main.favorites.FavoriteViewModel
import com.example.weather.ui.main.forecast.ForecastTabedActivity
import com.example.weather.util.Constants
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesCityNewFragment : MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding

    private val citiesWeatherListAdapter = WeatherCitiesAdapterVertical(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavoritesCityNewFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        viewModel.getFavoritesWeather()
    }

    override fun initializeView() {
        binding.favoritesRecyclerView.apply {
            adapter = citiesWeatherListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun observeViewModel() {
        viewModel.favoriteCitiesWeather.doOnChange(this) {
            binding.favoritesStatusView.visibility =
                if (it.isNotEmpty()) View.GONE else View.VISIBLE
            citiesWeatherListAdapter.setDataList(it)
        }

        viewModel.cityWeatherOnChange.doOnChange(this) {
            it.cityId?.let {
                viewModel.updateFavoriteStatus(it)
            }
        }
    }

    override fun onFavoriteClick(cityID: Int) {
        viewModel.updateFavoriteStatus(cityID)
    }

    override fun onItemClick(cityName: String, cityID: Int) {
        requireActivity().run {
            startActivity(
                Intent(this, ForecastTabedActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_CITY_NAME, cityName)
                        putExtra(Constants.EXTRA_CITY_ID, cityID)
                    }
            )
        }
    }

}