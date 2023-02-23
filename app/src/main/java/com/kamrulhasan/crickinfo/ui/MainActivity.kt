package com.kamrulhasan.crickinfo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_host) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        binding.bottomNavBar.setupWithNavController(navController) //

        binding.bottomNavBar.setOnItemSelectedListener {
            Log.d(TAG, "onCreate: home fragment1")
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.fixturesFragment -> {
                    navController.navigate(R.id.fixturesFragment)
                    true
                }
               R.id.playersFragment -> {
                    navController.navigate(R.id.playersFragment)
                    true
                }
                R.id.newsFragment -> {
                    navController.navigate(R.id.newsFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}