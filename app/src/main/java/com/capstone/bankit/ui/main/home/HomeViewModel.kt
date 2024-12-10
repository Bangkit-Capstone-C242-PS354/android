package com.capstone.bankit.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.models.TransactionResponse
import com.capstone.bankit.data.models.UserResponse
import com.capstone.bankit.data.repository.BankitRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val bankitRepository: BankitRepository
): ViewModel() {
    val tabFlag = MutableLiveData(0)

    private val _expenses = MutableLiveData<ExpenseResponse>()
    val expense: LiveData<ExpenseResponse> = _expenses

    private val _incomes = MutableLiveData<IncomeResponse>()
    val incomes: LiveData<IncomeResponse> = _incomes

    private val _transactions = MutableLiveData<TransactionResponse>()
    val transactions: LiveData<TransactionResponse> = _transactions

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

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

    fun getTransaction(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                onLoading(false)
                val transactionResponse = bankitRepository.getAllTransactions(token)

                _transactions.postValue(transactionResponse)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}", )
            }
        }
    }

    fun getFilteredTransaction(
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
                _transactions.value = TransactionResponse(null)
                onLoading(true)
                val transactionResponse = bankitRepository.getFilteredTransactions(token, period, startDate, endDate)
                _transactions.value = transactionResponse

            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}")
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

    fun getUser(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit,
        onSuccess: (UserResponse) -> Unit
    ){
        onLoading(true)
        viewModelScope.launch {
            try {
                onLoading(false)
                val userResponse = bankitRepository.getUser(token)
                _name.postValue(userResponse.data?.username ?: "Undefined")
                onSuccess(userResponse)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}", )
            }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}