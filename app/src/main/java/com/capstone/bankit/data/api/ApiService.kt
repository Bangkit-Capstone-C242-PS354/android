package com.capstone.bankit.data.api

import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomePostResponse
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.models.LoginRequest
import com.capstone.bankit.data.models.LoginResponse
import com.capstone.bankit.data.models.RegisterRequest
import com.capstone.bankit.data.models.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/signup")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/auth/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/expenses")
    suspend fun getAllExpenses(
        @Header("Authorization") token: String
    ): ExpenseResponse

    @GET("/expenses")
    suspend fun getFilteredExpenses(
        @Header("Authorization") token: String,
        @Query("period") period: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): ExpenseResponse

    @GET("/incomes")
    suspend fun getAllIncomes(
        @Header("Authorization") token: String
    ): IncomeResponse

    @GET("/incomes")
    suspend fun getFilteredIncomes(
        @Header("Authorization") token: String,
        @Query("period") period: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): IncomeResponse

    @POST("/incomes")
    suspend fun postIncome(
        @Header("Authorization") token: String,
        @Body request: IncomeRequest
    ): IncomePostResponse

    @POST("/expenses")
    suspend fun postExpense(
        @Header("Authorization") token: String,
        @Body request: ExpenseRequest
    ): ExpensePostResponse
}