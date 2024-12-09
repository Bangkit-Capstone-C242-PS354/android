package com.capstone.bankit.data.repository

import com.capstone.bankit.data.api.ApiService
import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomePostResponse
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.data.models.IncomeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}
