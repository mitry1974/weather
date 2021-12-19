package com.example.weather.ui.main.forecast

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.ui.main.forecast.SectionsPagerAdapter
import com.example.weather.databinding.ActivityForecastTabedBinding
import com.example.weather.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastTabedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastTabedBinding

    private var cityName: String = ""
    private var cityId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForecastTabedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent?.hasExtra(Constants.EXTRA_CITY_NAME) == true) {
            cityName = intent.getStringExtra(Constants.EXTRA_CITY_NAME) ?: ""
        }
        if (intent?.hasExtra(Constants.EXTRA_CITY_ID) == true) {
            cityId = intent.getIntExtra(Constants.EXTRA_CITY_ID, 0)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, cityId)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()

        supportActionBar?.title = "Weather for $cityName"


//        viewModel.getForecast(cityId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}