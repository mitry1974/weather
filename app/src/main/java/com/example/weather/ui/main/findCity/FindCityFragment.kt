package com.example.weather.ui.main.findCity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentFindCityBinding
import com.example.weather.ui.adapters.CitiesListPagingAdapter
import com.example.weather.ui.adapters.OnItemClickCallback
import com.example.weather.ui.common.MainNavigationFragment
import com.example.weather.util.doOnChange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@AndroidEntryPoint
class FindCityFragment: MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FindCityViewModelWithPaging by viewModels()
    private lateinit var binding: FragmentFindCityBinding
    private val citiesListAdapter = CitiesListPagingAdapter(this)

    private lateinit var requestMultiplePermissionLauncher: ActivityResultLauncher<Array<String>>

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

        requestMultiplePermissionLauncher = registerPermissionsLauncher()

        observeViewModel()
        return binding.root

    }

    override fun observeViewModel() {
        viewModel.isLoading.doOnChange(this) {
            binding.citiesListLoading.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.citiesListErrorView.visibility = View.GONE
                showToast("Выполняется загрузка списка городов")
            } else {
                showToast("Список городов загружен")
            }
        }

        viewModel.isGPSGetting.doOnChange(this) {
            binding.citiesListLoading.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                binding.citiesListErrorView.visibility = View.GONE
                showToast("Выполняется получение GPS координат")
            } else {
                showToast("GPS координаты получены")
            }
        }

        viewModel.toastError.doOnChange(this) {
            showToast(it, Toast.LENGTH_LONG)
        }

        viewModel.cityOnChange.doOnChange(this) {
            it.let {
                showToast(
                    if (it.isFavorite) "${it.name} добавлен в избранное"
                    else "${it.name} удален из избранного"
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        viewModel.checkFirstRunAndLoadData()

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

    override fun onItemClick(cityName:String, cityID: Int) {}

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context, message, duration).show()

    private fun checkLocationPermissions() =
        ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

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

}