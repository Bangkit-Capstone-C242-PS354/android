package com.capstone.bankit.ui.editprofile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.bankit.databinding.ActivityEditProfileBinding
import com.capstone.bankit.ui.changepassword.ChangePasswordActivity

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener { v -> finish() }
        binding.btnSave.setOnClickListener { v -> finish() }

        binding.btnChangePass.setOnClickListener { v ->
            startActivity(
                Intent(
                    this,
                    ChangePasswordActivity::class.java
                )
            )
        }
    }
}