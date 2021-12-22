package com.example.weather.ui.main.favorites

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoritesBinding
import com.example.weather.ui.adapters.OnItemClickCallback
import com.example.weather.ui.common.MainNavigationFragment
import com.example.weather.ui.adapters.WeatherCitiesAdapterVertical
import com.example.weather.ui.main.forecast.ForecastActivity
import com.example.weather.util.Constants
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity




@AndroidEntryPoint
class FavoritesCityFragment : MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding

    private val citiesWeatherListAdapter = WeatherCitiesAdapterVertical(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavoritesCityFragment.viewModel
            }
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        viewModel.getFavoritesWeather()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reload_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
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

        viewModel.toastError.doOnChange(this) {
            showToast(it, Toast.LENGTH_LONG)
        }

        viewModel.isLoading.doOnChange(this) {
            binding.favoritesListLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onFavoriteClick(cityID: Int) {
        viewModel.updateFavoriteStatus(cityID)
    }

    override fun onItemClick(cityName: String, cityID: Int) {
        requireActivity().run {
            startActivity(
                Intent(this, ForecastActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_CITY_NAME, cityName)
                        putExtra(Constants.EXTRA_CITY_ID, cityID)
                    }
            )
        }
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, message, duration).show()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh_reload -> viewModel.getFavoritesWeather()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}