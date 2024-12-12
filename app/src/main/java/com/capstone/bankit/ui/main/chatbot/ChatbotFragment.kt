package com.capstone.bankit.ui.main.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bankit.databinding.FragmentChatbotBinding
import com.capstone.bankit.ui.adapter.ChatAdapter
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.capstone.bankit.ui.main.chatbot.ChatbotViewModel
import com.capstone.bankit.data.models.ChatMessage

class ChatbotFragment : Fragment() {
    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatbotViewModel: ChatbotViewModel
    private lateinit var token: String
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        chatbotViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[ChatbotViewModel::class.java]
        token = TokenManager.getInstance(requireContext())?.token.toString()

        setupViews()
        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupViews() {
        binding.tilMessage.setEndIconOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                chatbotViewModel.sendMessage(
                    "Bearer $token",
                    message,
                    onFailure = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    },
                    onLoading = { isLoading ->
                        binding.tilMessage.isEnabled = !isLoading
                    }
                )
                binding.etMessage.text?.clear()
            }
        }
    }

    private fun observeViewModel() {
        chatbotViewModel.chatMessages.observe(viewLifecycleOwner) { messages: List<ChatMessage> ->
            chatAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                val position: Int = messages.size - 1
                binding.rvChat.post {
                    binding.rvChat.smoothScrollToPosition(position)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 