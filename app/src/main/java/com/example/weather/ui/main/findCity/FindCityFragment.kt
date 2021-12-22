package com.example.weather.ui.main.findCity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.databinding.FragmentFindCityBinding
import com.example.weather.ui.adapters.CitiesListPagingAdapter
import com.example.weather.ui.adapters.OnItemClickCallback
import com.example.weather.ui.common.MainNavigationFragment
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
class FindCityFragment : MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FindCityViewModelWithPaging by viewModels()
    private lateinit var binding: FragmentFindCityBinding
    private val citiesListAdapter = CitiesListPagingAdapter(this)

    private lateinit var requestMultiplePermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindCityBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FindCityFragment.viewModel
            }

        (activity as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)

        requestMultiplePermissionLauncher = registerPermissionsLauncher()

        observeViewModel()
        return binding.root

    }

    override fun observeViewModel() {
        viewModel.isLoading.doOnChange(this) {
            binding.citiesListLoading.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.citiesListErrorView.visibility = View.GONE
                showToast(getString(R.string.notification_loading_cities_list))
            }
        }

        viewModel.isGPSGetting.doOnChange(this) {
            binding.citiesListLoading.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.citiesListErrorView.visibility = View.GONE
                showToast(getString(R.string.notification_get_gps_coordinates))
            } else {
                showToast(getString(R.string.notification_got_gps_coordinates))
            }
        }

        viewModel.toastError.doOnChange(this) {
            showToast(it, Toast.LENGTH_LONG)
        }

        viewModel.cityOnChange.doOnChange(this) {
            it.let {
                showToast(
                    if (it.isFavorite) "${it.name} ${getString(R.string.notification_added_to_favorites)}"
                    else "${it.name} ${getString(R.string.notification_removed_from_favorites)}"
                )
            }
        }

        viewModel.homeCityName.doOnChange(this) {
            binding.locationSearch.setQuery(it, false)
        }

        viewModel.location.doOnChange(this) {
            viewModel.getLocationByCoordinates(it.lat, it.lon)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reload_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        viewModel.checkFirstRunAndLoadCitiesList()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getAllCities().collectLatest {
                citiesListAdapter.submitData(it)
            }
        }

    }

    override fun initializeView() {
        binding.citiesListRecyclerView.apply {
            adapter = citiesListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.locationSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.query = query ?: ""
                citiesListAdapter.refresh()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.query = newText ?: ""
                citiesListAdapter.refresh()
                return false
            }
        })

        binding.gpsLocation.setOnClickListener()
        {
            if (checkLocationPermissions()) {
                requestPermissions()
            } else {
                viewModel.getLocation()
            }
        }
    }

    override fun onFavoriteClick(id: Int) {
        viewModel.updateFavoriteStatus(id)
    }

    override fun onItemClick(cityName: String, cityID: Int) {}

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, message, duration).show()

    private fun showToast(messageId: Int, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, getString(messageId), duration).show()

    private fun checkLocationPermissions() =
        context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        } ?: false

    private fun registerPermissionsLauncher(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { (_, it) -> it }) {
                viewModel.getLocation()
            } else {
                showToast("Отсутствуют необходимые разрешения для получения gps координат")
            }
        }
    }

    private fun requestPermissions() {
        requestMultiplePermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh_reload -> viewModel.loadCitiesList()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}