package com.capstone.bankit.ui.auth.starter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.FragmentStarterBinding;

public class StarterFragment extends Fragment {
    private FragmentStarterBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStarterBinding.inflate(inflater, container, false);

        setListeners();

        return binding.getRoot();
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            NavDirections action = StarterFragmentDirections.actionNavigationStarterToNavigationLogin();
            Navigation.findNavController(v).navigate(action);
        });

        binding.btnRegister.setOnClickListener(v -> {
            NavDirections action = StarterFragmentDirections.actionNavigationStarterToNavigationRegister();
            Navigation.findNavController(v).navigate(action);
        });
    }
}