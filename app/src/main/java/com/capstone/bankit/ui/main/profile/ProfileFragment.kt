package com.capstone.bankit.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.bankit.databinding.FragmentProfileBinding
import com.capstone.bankit.ui.auth.AuthActivity
import com.capstone.bankit.ui.editprofile.EditProfileActivity
import com.capstone.bankit.utils.TokenManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.btnEdit.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    EditProfileActivity::class.java
                )
            )
        }
        binding.btnLogout.setOnClickListener {
            TokenManager.getInstance(requireContext())?.clearToken()
            startActivity(Intent(
                activity, AuthActivity::class.java
            ))
            activity?.finish()
        }
    }
}