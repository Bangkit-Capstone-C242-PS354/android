package com.capstone.bankit.ui.main.analytics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.models.TransactionModel
import com.capstone.bankit.data.repository.BankitRepository
import com.capstone.bankit.ui.main.home.HomeViewModel.Companion.TAG
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.Constants.filterByFlagExpenseIncome
import com.capstone.bankit.utils.DataHelper.generateTransactions
import kotlinx.coroutines.launch

class AnalyticsViewModel(
    private val bankitRepository: BankitRepository
) : ViewModel() {
    var tabFlag: MutableLiveData<Int> = MutableLiveData(1)

    private val _expenses = MutableLiveData<ExpenseResponse>()
    val expense: LiveData<ExpenseResponse> = _expenses

    private val _incomes = MutableLiveData<IncomeResponse>()
    val incomes: LiveData<IncomeResponse> = _incomes

    fun getExpense(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        onLoading(true)
        viewModelScope.launch {
            try {
                onLoading(false)
                val expenseResponse = bankitRepository.getAllExpenses(token)

                _expenses.postValue(expenseResponse)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getExpense: ${e.message}", )
            }
        }
    }

    fun getIncome(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                onLoading(false)
                val expenseResponse = bankitRepository.getAllIncomes(token)

                _incomes.postValue(expenseResponse)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}", )
            }
        }
    }

    fun getFilteredIncome(
        token: String,
        period: String,
        startDate: String,
        endDate: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                _incomes.value = IncomeResponse(null)
                onLoading(true)
                val incomeResponse = bankitRepository.getFilteredIncomes(token, period, startDate, endDate)
                _incomes.value = incomeResponse

            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}")
            }
        }
    }

    fun getFilteredExpense(
        token: String,
        period: String,
        startDate: String,
        endDate: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                _expenses.value = ExpenseResponse(null)
                val expenseResponse = bankitRepository.getFilteredExpenses(token, period, startDate, endDate)
                _expenses.value = expenseResponse

            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getExpense: ${e.message}")
            }
        }
    }

}