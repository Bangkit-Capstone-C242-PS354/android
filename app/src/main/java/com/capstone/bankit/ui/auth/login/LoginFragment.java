package com.capstone.bankit.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.capstone.bankit.data.api.ApiClient;
import com.capstone.bankit.data.api.AuthService;
import com.capstone.bankit.data.models.LoginRequest;
import com.capstone.bankit.data.models.LoginResponse;
import com.capstone.bankit.databinding.FragmentLoginBinding;
import com.capstone.bankit.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            // Capture the input values
            String email = binding.edEmail.getText().toString().trim();
            String password = binding.edPassword.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform login
            login(email, password);
        });

        binding.btnRegister.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionNavigationLoginToNavigationRegister();
            Navigation.findNavController(v).navigate(action);
        });

        binding.btnForgotPassword.setOnClickListener(v -> {
            // Handle forgot password action here (e.g., navigate to reset password screen)
        });
    }

    private void login(String email, String password) {
        // Create the login request object
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Create Retrofit service and make the API call
        AuthService authService = ApiClient.getInstance().create(AuthService.class);
        authService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatusCode() == 200) {
                        // Login success: Navigate to MainActivity
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(requireActivity(), MainActivity.class));
                        requireActivity().finish(); // Close the login activity
                    } else {
                        // Show error message from API
                        Toast.makeText(requireContext(), "Login failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show failure message if response is not successful
                    Toast.makeText(requireContext(), "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Handle failure scenario (e.g., no internet connection)
                Toast.makeText(requireContext(), "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}