package com.capstone.bankit.ui.editprofile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.data.models.UpdateUserRequest
import com.capstone.bankit.databinding.ActivityEditProfileBinding
import com.capstone.bankit.ui.changepassword.ChangePasswordActivity
import com.capstone.bankit.ui.main.MainActivity
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(this, ViewModelFactory(this))[ProfileViewModel::class.java]
        setListeners()
    }

    private fun setListeners() {
        val email = TokenManager.getInstance(this)?.email
        val name = TokenManager.getInstance(this)?.name

        binding.edEmail.isEnabled = false
        binding.edPassword.isEnabled = false

        binding.edEmail.setText(email)
        binding.edUsername.setText(name)
        binding.tvUsername.text = name

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.edUsername.text.toString()
            val newEmail = binding.edEmail.text.toString()
            val mode = binding.switchNightMode.isChecked
            val token = TokenManager.getInstance(this)?.token

            if (newName.isEmpty()){
                Toast.makeText(this, "Please check your username and email field, don't let it empty", Toast.LENGTH_SHORT).show()
            } else {
                profileViewModel.updateUsername(
                    token = "Bearer $token",
                    request = UpdateUserRequest(
                        username = newName
                    ),
                    onFailure = { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {
                        TokenManager.getInstance(this)?.saveToken(
                            token = token,
                            email = TokenManager.getInstance(this)?.email,
                            name = newName
                        )
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("navigate", "profile")
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }

        binding.btnChangePass.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ChangePasswordActivity::class.java
                )
            )
        }
    }
}