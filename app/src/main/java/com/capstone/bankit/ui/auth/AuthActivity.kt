package com.capstone.bankit.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.capstone.bankit.R
import com.capstone.bankit.databinding.ActivityAuthBinding
import com.capstone.bankit.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)

        if (currentUser != null) {
            if (!token.isNullOrEmpty()) {
                // Token exists, navigate to MainActivity
                val intent = Intent(
                    this@AuthActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
                return
            }
        }

        // Token does not exist, proceed with AuthActivity
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavController for fragment navigation
        val navController = findNavController(this, R.id.nav_host_fragment_activity_auth)

        // If you want to use navigation, here is where you can implement it
        // For example, this can navigate to the login fragment if needed
        // navController.navigate(R.id.action_to_loginFragment)
    }

    companion object {
        // SharedPreferences constants
        private const val PREFS_NAME = "AppPreferences"
        private const val TOKEN_KEY = "auth_token"
    }
}
