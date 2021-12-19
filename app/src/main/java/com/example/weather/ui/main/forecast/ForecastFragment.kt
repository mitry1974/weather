package com.example.weather.ui.main.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentForecastTabedBinding
import com.example.weather.ui.adapters.CitiesWeatherListAdapter
import com.example.weather.ui.adapters.ForecastListAdapter
import com.example.weather.util.Constants
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastFragment : Fragment() {
    private lateinit var viewModel: ForecastViewModel
    private var _binding: FragmentForecastTabedBinding? = null

    private val forecastListAdapter = ForecastListAdapter()

    private val binding get() = _binding!!

    var cityId: Int = 0
    var forecastDays: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ForecastViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
            setForecastInfo(arguments?.getInt(CITY_ID), arguments?.getInt(FORECAST_DAYS))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentForecastTabedBinding.inflate(inflater, container, false)
            .apply{
                viewModel = this@ForecastFragment.viewModel
                lifecycleOwner = viewLifecycleOwner
            }

        observeViewModel()

        viewModel.getForecast()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
    }

    private fun initializeView() {
        binding.forecastRecyclerView.apply {
            adapter = forecastListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel () {
        viewModel.text.doOnChange(this) {
            binding.sectionLabel.text = it
        }

        viewModel.forecastData.doOnChange(this) {
            forecastListAdapter.setDataList(it)
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val CITY_ID = "city_id"
        private const val FORECAST_DAYS = "forecast_days"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, cityId: Int, forecastDays: Int): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putInt(CITY_ID, cityId)
                    putInt(FORECAST_DAYS, forecastDays)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}