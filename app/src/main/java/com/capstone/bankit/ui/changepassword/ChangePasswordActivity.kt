package com.capstone.bankit.ui.changepassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.bankit.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener { v -> finish() }
        binding.btnChangePass.setOnClickListener { v -> finish() }
    }
}