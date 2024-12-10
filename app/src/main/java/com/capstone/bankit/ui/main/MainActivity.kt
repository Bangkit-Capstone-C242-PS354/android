package com.capstone.bankit.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.capstone.bankit.R
import com.capstone.bankit.databinding.ActivityMainBinding
import com.capstone.bankit.ui.auth.AuthActivity
import com.capstone.bankit.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val token = TokenManager.getInstance(this)?.token

        if (currentUser == null) {
            if (token.isNullOrEmpty()) {
                val intent = Intent(
                    this,
                    AuthActivity::class.java
                )
                startActivity(intent)
                finish()
                return
            }
        }

        val navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupWithNavController(binding.navView, navController)

        handleNavigationIntent(navController)
    }

    private fun handleNavigationIntent(navController: NavController) {
        val destination = intent.getStringExtra("navigate")
        when (destination) {
            "profile" -> {
                if (navController.currentDestination?.id != R.id.navigation_profile) {
                    navController.navigate(R.id.navigation_profile)
                }
            }

            "analytics" -> {
                if (navController.currentDestination?.id != R.id.navigation_analytics) {
                    navController.navigate(R.id.navigation_analytics)
                }
            }

            else -> {
                if (navController.currentDestination?.id != R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home)
                }
            }
        }
    }
}
