package com.example.weather.ui.main.forecast

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
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

        binding.toolbar.inflateMenu(R.menu.reload_menu)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title = "${getString(R.string.forecast_activity_caption)} $cityName"

        initializeView()
        observeViewModel()

        viewModel.getForecast()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.reload_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    private fun observeViewModel() {
        viewModel.forecastData.doOnChange(this) {
            viewModel.forecastData.value?.forecastRows?.let {
                forecastAdapter.setDataList(it)
            }
        }

        viewModel.toastError.doOnChange(this) {
            showToast(viewModel.toastError.value.toString())
        }

        viewModel.isLoading.doOnChange(this) {
            binding.forecastLoading.visibility = if (it) View.VISIBLE else View.GONE
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
            R.id.action_refresh_reload -> viewModel.getForecast()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, message, duration).show()

}