package com.capstone.bankit.ui.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.repository.BankitRepository
import java.io.File

class ExpenseViewModel(private val bankitRepository: BankitRepository) : ViewModel() {
    var selectedImageFile: MutableLiveData<File> = MutableLiveData()

    fun postExpense(
        token: String,
        request: ExpenseRequest
    ){

    }
}