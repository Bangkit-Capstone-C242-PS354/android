package com.capstone.bankit.ui.auth.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.FragmentLoginBinding;
import com.capstone.bankit.databinding.FragmentRegisterBinding;
import com.capstone.bankit.ui.auth.starter.StarterFragmentDirections;

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
        binding.btnLogin.setOnClickListener(v -> {
            NavDirections action = RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin();
            Navigation.findNavController(v).navigate(action);
        });

        binding.btnRegister.setOnClickListener(v -> {
            NavDirections action = RegisterFragmentDirections.actionNavigationRegisterToNavigationLogin();
            Navigation.findNavController(v).navigate(action);
        });
    }
}