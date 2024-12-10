package com.capstone.bankit.ui.auth.starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.capstone.bankit.databinding.FragmentStarterBinding

class StarterFragment : Fragment() {
    private lateinit var binding: FragmentStarterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStarterBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener { v ->
            val action =
                StarterFragmentDirections.actionNavigationStarterToNavigationLogin()
            findNavController(v).navigate(action)
        }

        binding.btnRegister.setOnClickListener { v ->
            val action =
                StarterFragmentDirections.actionNavigationStarterToNavigationRegister()
            findNavController(v).navigate(action)
        }
    }
}