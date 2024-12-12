package com.capstone.bankit.ui.main.chatbot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.ChatRequest
import com.capstone.bankit.data.models.ChatResponse
import com.capstone.bankit.data.models.ChatMessage
import com.capstone.bankit.data.repository.BankitRepository
import kotlinx.coroutines.launch

class ChatbotViewModel(
    private val bankitRepository: BankitRepository
) : ViewModel() {
    private val _chatResponse = MutableLiveData<ChatResponse>()
    val chatResponse: LiveData<ChatResponse> = _chatResponse
    
    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages
    
    private val messagesList = mutableListOf<ChatMessage>()

    init {
        messagesList.add(ChatMessage(
            content = "Hello! This is BankIT Chatbot. How can I help you?",
            isUser = false
        ))
        _chatMessages.value = messagesList.toList()
    }

    fun sendMessage(
        token: String,
        message: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        onLoading(true)
        // Add user message
        messagesList.add(ChatMessage(content = message, isUser = true))
        _chatMessages.value = messagesList.toList()
        
        viewModelScope.launch {
            try {
                val response = bankitRepository.chat(token, ChatRequest(message))
                messagesList.add(ChatMessage(content = response.data.reply, isUser = false))
                _chatMessages.value = messagesList.toList()
                _chatResponse.value = response
                onLoading(false)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "sendMessage: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "ChatbotViewModel"
    }
} 