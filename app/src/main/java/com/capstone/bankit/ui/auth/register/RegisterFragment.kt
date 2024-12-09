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
        val registerRequest = RegisterRequest(username, email, password)

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

                    // Step 5: Handle the successful response
                    if (registerResponse?.statusCode == 201) {
                        val successMessage = "Registration successful!"
                        binding.tvError.text = successMessage
                        binding.tvError.setTextColor(resources.getColor(R.color.primaryGreen))
                        binding.tvError.visibility = View.VISIBLE

                        // Navigate to login screen after successful registration
                        val action =
                            RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin()
                        findNavController(binding.root).navigate(action)
                    } else {
                        // Show error message from the response
                        binding.tvError.text = "Error: " + registerResponse?.message
                        binding.tvError.visibility = View.VISIBLE
                    }
                } else {
                    // Handle API failure or unsuccessful response
                    binding.tvError.text = "Registration failed: " + response.message()
                    binding.tvError.visibility = View.VISIBLE
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                // Handle network failure or other issues
                binding.tvError.text = "An error occurred: " + t.message
                binding.tvError.visibility = View.VISIBLE
            }
        })
    }
}