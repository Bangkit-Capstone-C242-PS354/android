package com.capstone.bankit.ui.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.UpdateUserRequest
import com.capstone.bankit.data.models.UpdateUserResponse
import com.capstone.bankit.data.repository.BankitRepository
import kotlinx.coroutines.launch
import java.io.IOException

class ProfileViewModel(private val bankitRepository: BankitRepository): ViewModel() {
    fun updateUsername(
        token: String,
        request: UpdateUserRequest,
        onSuccess: (UpdateUserResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = bankitRepository.updateUsername(token, request)
                onSuccess(response)
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }
}