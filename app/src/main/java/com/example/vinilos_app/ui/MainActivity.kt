package com.example.vinilos_app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyLog
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        VolleyLog.DEBUG = true

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.selectorsUp.selectorAlbums.setOnClickListener {
            if (navController.currentDestination?.id != R.id.albumFragment) {
                navController.navigate(R.id.albumFragment)
            }
        }

        binding.selectorsUp.selectorArtists.setOnClickListener{
            if(navController.currentDestination?.id != R.id.performerFragment){
                navController.navigate(R.id.performerFragment)
            }
        }

        binding.selectorsDown.selectorHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                navController.navigate(R.id.homeFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.selectorsUp.selectorAlbums.isEnabled = destination.id != R.id.albumFragment
            binding.selectorsDown.selectorHome.isEnabled = destination.id != R.id.homeFragment
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
