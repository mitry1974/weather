package com.example.weather

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavigationHostFragment) as? NavHostFragment ?: return

        val navController = findNavController(R.id.mainNavigationHostFragment)
        val mainBottomNavigationView = findViewById<BottomNavigationView>(R.id.mainBottomNavigationView)
        mainBottomNavigationView.setupWithNavController(navController)
    }
}