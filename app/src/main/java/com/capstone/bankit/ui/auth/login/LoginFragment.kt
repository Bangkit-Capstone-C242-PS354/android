package com.capstone.bankit.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.capstone.bankit.data.api.ApiConfig
import com.capstone.bankit.data.api.ApiService
import com.capstone.bankit.data.models.LoginRequest
import com.capstone.bankit.data.models.LoginResponse
import com.capstone.bankit.databinding.FragmentLoginBinding
import com.capstone.bankit.ui.main.MainActivity
import com.capstone.bankit.ui.main.home.HomeViewModel
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Initialize Firebase
        FirebaseApp.initializeApp(requireContext())
        mAuth = FirebaseAuth.getInstance()
        homeViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[HomeViewModel::class.java]

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            // Capture the input values
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter both email and password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Perform login
            login(email, password)
        }

        binding.btnRegister.setOnClickListener { v ->
            val action =
                LoginFragmentDirections.actionNavigationLoginToNavigationRegister()
            findNavController(v).navigate(action)
        }

        binding.btnForgotPassword.setOnClickListener {
            // Handle forgot password action here (e.g., navigate to reset password screen)
            // You can implement this feature as needed
            Toast.makeText(
                requireContext(),
                "Forgot Password feature is not implemented yet.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveToken(token: String?, email: String?, name: String?) {
        TokenManager.getInstance(requireContext())?.saveToken(token, email, name)
    }

    private fun login(email: String, password: String) {
        // Show ProgressBar and disable login button
        binding.btnLogin.isEnabled = false

        // Create the login request object
        val loginRequest = LoginRequest(email = email, password =  password)

        // Create Retrofit service and make the API call
        val authService: ApiService = ApiConfig.getApiService()
        authService.login(loginRequest).enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>,
            ) {
                // Hide ProgressBar and enable login button
                binding.btnLogin.isEnabled = true

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()

                    if (loginResponse!!.statusCode == 201) {
                        // Retrieve token from response
                        val data = loginResponse.data
                        if (data?.customToken != null) {
                            val customToken: String = data.customToken
                            Toast.makeText(
                                requireContext(),
                                "Login successful! Authenticating with Firebase...",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Sign in to Firebase with the custom token
                            signInWithCustomToken(customToken)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login failed: Missing token in response",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Other status codes are considered as failure
                        Toast.makeText(
                            requireContext(),
                            "Login failed: " + loginResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If response is not successful
                    Toast.makeText(
                        requireContext(),
                        "Login failed: " + response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                // Hide ProgressBar and enable login button
                binding.btnLogin.isEnabled = true

                // Handle failure scenario (e.g., no internet connection)
                Toast.makeText(requireContext(), "Login failed: " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }

            private fun signInWithCustomToken(customToken: String) {
                mAuth.signInWithCustomToken(customToken)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            val user = mAuth.currentUser
                            // Check if user is not null
                            user?.let {
                                val email = it.email

                                Log.d("User Info", "Email: $email")

                                // Retrieve Firebase ID token
                                it.getIdToken(true).addOnCompleteListener(requireActivity()) { task ->
                                    if (task.isSuccessful) {
                                        val idToken = task.result.token
                                        var name: String? = null

                                        homeViewModel.getUser(
                                            "Bearer $idToken",
                                            onFailure = {
                                                Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                                            },
                                            onLoading = {},
                                            onSuccess = { userResponse ->
                                                name = userResponse.data?.username
                                            }
                                        )

                                        saveToken(token = idToken, email = email, name = name)

                                        Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()

                                        navigateToMainActivity()
                                    } else {
                                        // Handle error -> task.getException()
                                        Toast.makeText(requireContext(), "Failed to retrieve ID token: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } ?: run {
                                Toast.makeText(requireContext(), "Authentication failed: User is null", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(requireContext(), "Firebase Authentication Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}