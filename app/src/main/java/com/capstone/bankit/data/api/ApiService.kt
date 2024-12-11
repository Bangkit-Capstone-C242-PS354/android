package com.capstone.bankit.data.api

import com.capstone.bankit.data.models.ExpenseDetailResponse
import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomePostResponse
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.models.LoginRequest
import com.capstone.bankit.data.models.LoginResponse
import com.capstone.bankit.data.models.PostReceiptResponse
import com.capstone.bankit.data.models.ReceiptCheckResponse
import com.capstone.bankit.data.models.RegisterRequest
import com.capstone.bankit.data.models.RegisterResponse
import com.capstone.bankit.data.models.TransactionResponse
import com.capstone.bankit.data.models.UpdateUserRequest
import com.capstone.bankit.data.models.UpdateUserResponse
import com.capstone.bankit.data.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import java.io.File

interface ApiService {
    @POST("/auth/signup")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/auth/signin")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/expenses")
    suspend fun getAllExpenses(
        @Header("Authorization") token: String
    ): ExpenseResponse

    @GET("/expenses/{expenses_id}")
    suspend fun getExpenseDetail(
        @Header("Authorization") token: String,
        @Path("expenses_id") expenseId: String
    ): ExpenseDetailResponse

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

    @GET("/transactions")
    suspend fun getAllTransactions(
        @Header("Authorization") token: String
    ): TransactionResponse

    @GET("/transactions")
    suspend fun getFilteredTransactions(
        @Header("Authorization") token: String,
        @Query("period") period: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): TransactionResponse

    @GET("/users/me")
    suspend fun getUserData(
        @Header("Authorization") token: String
    ): UserResponse

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

    @Multipart
    @POST("/receipts/upload")
    suspend fun postReceipt(
        @Header("Authorization") token: String,
        @Part receipt: MultipartBody.Part
    ): PostReceiptResponse

    @PUT("/users/me")
    suspend fun changeUsername(
        @Header("Authorization") token: String,
        @Body request: UpdateUserRequest
    ): UpdateUserResponse

    @GET("/receipts/check")
    suspend fun checkReceipt(
        @Header("Authorization") token: String,
        @Query("filename") filename: String
    ): ReceiptCheckResponse

    @Streaming
    @GET("/transactions/export")
    suspend fun exportTransactions(
        @Header("Authorization") token: String
    ): ResponseBody
}