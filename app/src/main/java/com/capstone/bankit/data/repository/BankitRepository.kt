package com.capstone.bankit.data.repository

import com.capstone.bankit.data.api.ApiService
import com.capstone.bankit.data.models.ExpenseDetailResponse
import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomePostResponse
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.models.PostReceiptResponse
import com.capstone.bankit.data.models.ReceiptCheckResponse
import com.capstone.bankit.data.models.TransactionResponse
import com.capstone.bankit.data.models.UpdateUserRequest
import com.capstone.bankit.data.models.UpdateUserResponse
import com.capstone.bankit.data.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class BankitRepository(private val apiService: ApiService) {

    /**
     * Fetches all expenses.
     *
     * @param token Authorization token.
     * @return ExpenseResponse containing the list of expenses.
     */
    suspend fun getAllExpenses(token: String): ExpenseResponse {
        return withContext(Dispatchers.IO) {
            apiService.getAllExpenses(token)
        }
    }

    suspend fun getExpenseDetail(token: String, expenseId: String): ExpenseDetailResponse {
        return withContext(Dispatchers.IO) {
            apiService.getExpenseDetail(token, expenseId)
        }
    }

    /**
     * Fetches filtered expenses based on the provided parameters.
     *
     * @param token Authorization token.
     * @param period The period for filtering expenses (e.g., daily, monthly).
     * @param startDate The start date for the expense filter.
     * @param endDate The end date for the expense filter.
     * @return ExpenseResponse containing the filtered list of expenses.
     */
    suspend fun getFilteredExpenses(
        token: String,
        period: String,
        startDate: String,
        endDate: String
    ): ExpenseResponse {
        return withContext(Dispatchers.IO) {
            apiService.getFilteredExpenses(token, period, startDate, endDate)
        }
    }

    /**
     * Fetches all incomes.
     *
     * @param token Authorization token.
     * @return IncomeResponse containing the list of incomes.
     */
    suspend fun getAllIncomes(token: String): IncomeResponse {
        return withContext(Dispatchers.IO) {
            apiService.getAllIncomes(token)
        }
    }

    suspend fun getAllTransactions(token: String): TransactionResponse {
        return withContext(Dispatchers.IO) {
            apiService.getAllTransactions(token)
        }
    }

    /**
     * Fetches filtered incomes based on the provided parameters.
     *
     * @param token Authorization token.
     * @param period The period for filtering incomes (e.g., daily, monthly).
     * @param startDate The start date for the income filter.
     * @param endDate The end date for the income filter.
     * @return IncomeResponse containing the filtered list of incomes.
     */
    suspend fun getFilteredIncomes(
        token: String,
        period: String,
        startDate: String,
        endDate: String
    ): IncomeResponse {
        return withContext(Dispatchers.IO) {
            apiService.getFilteredIncomes(token, period, startDate, endDate)
        }
    }

    suspend fun getFilteredTransactions(
        token: String,
        period: String,
        startDate: String,
        endDate: String
    ): TransactionResponse {
        return withContext(Dispatchers.IO) {
            apiService.getFilteredTransactions(token, period, startDate, endDate)
        }
    }

    suspend fun postIncome(
        token: String,
        request: IncomeRequest
    ): IncomePostResponse {
        return withContext(Dispatchers.IO) {
            apiService.postIncome(token, request)
        }
    }

    suspend fun postExpense(
        token: String,
        request: ExpenseRequest
    ): ExpensePostResponse {
        return withContext(Dispatchers.IO) {
            apiService.postExpense(token, request)
        }
    }

    suspend fun postReceipt(
        token: String,
        receipt: MultipartBody.Part
    ): PostReceiptResponse {
        return withContext(Dispatchers.IO){
            apiService.postReceipt(token, receipt)
        }
    }

    suspend fun getUser(
        token: String
    ): UserResponse {
        return withContext(Dispatchers.IO) {
            apiService.getUserData(token)
        }
    }

    suspend fun updateUsername(
        token: String,
        request: UpdateUserRequest
    ): UpdateUserResponse {
        return withContext(Dispatchers.IO) {
            apiService.changeUsername(token, request)
        }
    }

    suspend fun receiptCheck(
        token: String,
        filename: String
    ): ReceiptCheckResponse {
        return withContext(Dispatchers.IO) {
            apiService.checkReceipt(token, filename)
        }
    }
}
