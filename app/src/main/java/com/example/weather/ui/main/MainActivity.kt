package com.example.weather.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.weather.R
import com.example.weather.ui.common.NavigationHost
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {
    private val viewModel: MainViewModel by viewModels()

    companion object {
        private val TOOLBAR_DESTINATION = setOf(
            R.id.navigation_favorites,
            R.id.navigation_find_location
        )
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavigationHostFragment) as? NavHostFragment
                ?: return

        navController = findNavController(R.id.mainNavigationHostFragment)
        val mainBottomNavigationView =
            findViewById<BottomNavigationView>(R.id.mainBottomNavigationView)

        appBarConfiguration = AppBarConfiguration(TOOLBAR_DESTINATION)

        navController?.apply {
            mainBottomNavigationView.setupWithNavController(this)
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        navController?.apply {
            toolbar.setupWithNavController(this, appBarConfiguration)
        }
    }
}