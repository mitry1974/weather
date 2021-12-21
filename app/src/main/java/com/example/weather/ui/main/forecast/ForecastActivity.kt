package com.example.weather.ui.main.forecast

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ActivityForecastBinding
import com.example.weather.ui.adapters.ForecastAdapterVertical
import com.example.weather.util.Constants
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastActivity: AppCompatActivity() {
    private lateinit var binding: ActivityForecastBinding

    private val viewModel: ForecastViewModel by viewModels()

    private val forecastAdapter = ForecastAdapterVertical()

    private var cityName: String = ""
    private var cityId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForecastBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@ForecastActivity
            viewModel = this@ForecastActivity.viewModel
        }
        setContentView(binding.root)

        if (intent?.hasExtra(Constants.EXTRA_CITY_NAME) == true) {
            cityName = intent.getStringExtra(Constants.EXTRA_CITY_NAME) ?: ""
        }
        if (intent?.hasExtra(Constants.EXTRA_CITY_ID) == true) {
            cityId = intent.getIntExtra(Constants.EXTRA_CITY_ID, 0)
        }
        viewModel.setForecastInfo(cityId)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = "Weather for $cityName"

        initializeView()
        observeViewModel()

        viewModel.getForecast()
    }

    private fun observeViewModel() {
        viewModel.forecastData.doOnChange(this) {
            viewModel.forecastData.value?.forecastRows?.let {
                forecastAdapter.setDataList(it)
            }
        }
    }

    private fun initializeView() {
        binding.forecastRecyclerView.apply {
            adapter = forecastAdapter
            layoutManager =  LinearLayoutManager(context)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}