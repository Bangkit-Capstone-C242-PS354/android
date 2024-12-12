package com.capstone.bankit.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.data.di.Injection
import com.capstone.bankit.ui.editprofile.ProfileViewModel
import com.capstone.bankit.ui.expense.ExpenseViewModel
import com.capstone.bankit.ui.income.IncomeViewModel
import com.capstone.bankit.ui.main.analytics.AnalyticsViewModel
import com.capstone.bankit.data.repository.BankitRepository
import com.capstone.bankit.ui.main.home.HomeViewModel
import com.capstone.bankit.ui.main.chatbot.ChatbotViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java) -> {
                AnalyticsViewModel(
                    Injection.provideRepository(context),
                    context
                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    Injection.provideRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(ExpenseViewModel::class.java) -> {
                ExpenseViewModel(
                    Injection.provideRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(IncomeViewModel::class.java) -> {
                IncomeViewModel(
                    Injection.provideRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    Injection.provideRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(ChatbotViewModel::class.java) -> {
                ChatbotViewModel(
                    Injection.provideRepository(context)
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}