package com.capstone.bankit.data.di

import android.content.Context
import com.capstone.bankit.data.api.ApiConfig
import com.capstone.bankit.data.api.ApiService
import com.capstone.bankit.data.repository.BankitRepository

object Injection {
    fun provideRepository(context: Context): BankitRepository {
        val apiService: ApiService = ApiConfig.getApiService()
        return BankitRepository(apiService)
    }
}