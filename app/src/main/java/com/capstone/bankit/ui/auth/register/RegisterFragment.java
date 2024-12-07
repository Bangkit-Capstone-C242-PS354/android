package com.capstone.bankit.ui.auth.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.bankit.R;
import com.capstone.bankit.data.api.ApiClient;
import com.capstone.bankit.data.api.AuthService;
import com.capstone.bankit.data.models.RegisterRequest;
import com.capstone.bankit.data.models.RegisterResponse;
import com.capstone.bankit.databinding.FragmentLoginBinding;
import com.capstone.bankit.databinding.FragmentRegisterBinding;
import com.capstone.bankit.ui.auth.starter.StarterFragmentDirections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        // Navigate to login screen
        binding.btnLogin.setOnClickListener(v -> {
            NavDirections action = RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin();
            Navigation.findNavController(v).navigate(action);
        });

        // Trigger registration API call
        binding.btnRegister.setOnClickListener(v -> {
            performRegistration();
        });
    }

    private void performRegistration() {
        // Step 1: Get user input
        String username = binding.edUsername.getText().toString().trim();
        String email = binding.edEmail.getText().toString().trim();
        String password = binding.edPassword.getText().toString().trim();
        String confirmPassword = binding.edConfirmPassword.getText().toString().trim();

        // Step 2: Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            binding.tvError.setText("All fields are required!");
            binding.tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.tvError.setText("Passwords do not match!");
            binding.tvError.setVisibility(View.VISIBLE);
            return;
        }

        // Step 3: Create the request object
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        // Step 4: Create Retrofit service and make the API call
        AuthService authService = ApiClient.getInstance().create(AuthService.class);
        authService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();

                    // Step 5: Handle the successful response
                    if (registerResponse.getStatusCode() == 201) {
                        String successMessage = "Registration successful!";
                        binding.tvError.setText(successMessage);
                        binding.tvError.setTextColor(getResources().getColor(R.color.primaryGreen));
                        binding.tvError.setVisibility(View.VISIBLE);

                        // Navigate to login screen after successful registration
                        NavDirections action = RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin();
                        Navigation.findNavController(binding.getRoot()).navigate(action);
                    } else {
                        // Show error message from the response
                        binding.tvError.setText("Error: " + registerResponse.getMessage());
                        binding.tvError.setVisibility(View.VISIBLE);
                    }
                } else {
                    // Handle API failure or unsuccessful response
                    binding.tvError.setText("Registration failed: " + response.message());
                    binding.tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Handle network failure or other issues
                binding.tvError.setText("An error occurred: " + t.getMessage());
                binding.tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}