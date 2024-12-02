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

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.FragmentLoginBinding;
import com.capstone.bankit.databinding.FragmentStarterBinding;
import com.capstone.bankit.ui.auth.starter.StarterFragmentDirections;
import com.capstone.bankit.ui.main.MainActivity;


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
            requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
        });

        binding.btnRegister.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionNavigationLoginToNavigationRegister();
            Navigation.findNavController(v).navigate(action);
        });
    }
}