package com.capstone.bankit.ui.auth.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.capstone.bankit.R
import com.capstone.bankit.data.api.ApiConfig
import com.capstone.bankit.data.models.RegisterRequest
import com.capstone.bankit.data.models.RegisterResponse
import com.capstone.bankit.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        // Navigate to login screen
        binding.btnLogin.setOnClickListener { v ->
            val action =
                RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin()
            findNavController(v).navigate(action)
        }

        // Trigger registration API call
        binding.btnRegister.setOnClickListener {
            performRegistration()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun performRegistration() {
        // Step 1: Get user input
        val username = binding.edUsername.text.toString().trim()
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val confirmPassword = binding.edConfirmPassword.text.toString().trim()

        // Step 2: Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            binding.tvError.text = "All fields are required!"
            binding.tvError.visibility = View.VISIBLE
            return
        }

        if (password != confirmPassword) {
            binding.tvError.text = "Passwords do not match!"
            binding.tvError.visibility = View.VISIBLE
            return
        }

        // Step 3: Create the request object
        val registerRequest = RegisterRequest(
            username = username,
            email = email,
            password = password
        )

        // Step 4: Create Retrofit service and make the API call
        val authService = ApiConfig.getApiService()
        authService.register(registerRequest).enqueue(object : Callback<RegisterResponse?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RegisterResponse?>,
                response: Response<RegisterResponse?>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()

                    if (registerResponse?.statusCode == 201) {
                        val successMessage = "Registration successful!"
                        binding.tvError.text = successMessage
                        binding.tvError.setTextColor(resources.getColor(R.color.primaryGreen))
                        binding.tvError.visibility = View.VISIBLE

                        val action = RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin()
                        findNavController(binding.root).navigate(action)
                    } else {
                        showError(registerResponse?.message ?: "Unknown error occurred")
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            if (errorBody.contains("message")) {
                                // Check if message is an array
                                if (errorBody.contains("message\":[")) {
                                    // Handle array of messages
                                    val messages = errorBody.substringAfter("message\":[")
                                        .substringBefore("]")
                                        .split(",")
                                        .map { it.trim('"') }
                                        .joinToString("\n") // Join messages with newline
                                    showError(messages)
                                } else {
                                    // Handle single message
                                    val message = errorBody.substringAfter("message\":\"")
                                        .substringBefore("\"")
                                        .replace("\\", "")
                                    showError(message)
                                }
                            } else {
                                showError("Registration failed")
                            }
                        } else {
                            showError("Registration failed")
                        }
                    } catch (e: Exception) {
                        showError("Registration failed")
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                showError("Network error occurred")
            }

            private fun showError(message: String) {
                binding.tvError.text = message
                binding.tvError.visibility = View.VISIBLE
            }
        })
    }
}