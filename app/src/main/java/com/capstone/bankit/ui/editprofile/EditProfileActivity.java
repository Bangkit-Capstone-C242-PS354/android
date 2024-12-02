package com.capstone.bankit.ui.editprofile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.ActivityEditProfileBinding;
import com.capstone.bankit.ui.changepassword.ChangePasswordActivity;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> finish());

        binding.btnChangePass.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });
    }
}