package com.capstone.bankit.ui.income

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.IncomePostResponse
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.data.repository.BankitRepository
import com.capstone.bankit.ui.main.home.HomeViewModel.Companion.TAG
import kotlinx.coroutines.launch
import java.io.IOException
import com.capstone.bankit.data.models.IncomeDetailResponse

class IncomeViewModel(private val bankitRepository: BankitRepository): ViewModel() {

    private val _expenses = MutableLiveData<IncomePostResponse>()
    val expenses: LiveData<IncomePostResponse> = _expenses

    private val _incomeDetail = MutableLiveData<IncomeDetailResponse>()
    val incomeDetail: LiveData<IncomeDetailResponse> = _incomeDetail

    fun postIncome(
        token: String,
        request: IncomeRequest,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                onLoading(false)
                val expenseResponse = bankitRepository.postIncome(token, request)

                _expenses.postValue(expenseResponse)
                onSuccess()
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getExpense: ${e.message}", )
            }
        }
    }

    fun getIncomeDetail(
        token: String,
        incomeId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = bankitRepository.getIncomeDetail(token, incomeId)
                _incomeDetail.value = response
                onSuccess()
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }
}