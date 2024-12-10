package com.capstone.bankit.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.data.di.Injection
import com.capstone.bankit.ui.editprofile.ProfileViewModel
import com.capstone.bankit.ui.expense.ExpenseViewModel
import com.capstone.bankit.ui.income.IncomeViewModel
import com.capstone.bankit.ui.main.analytics.AnalyticsViewModel
import com.capstone.bankit.ui.main.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                Injection.provideRepository(context)
            ) as T
        }
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(
                Injection.provideRepository(context)
            ) as T
        }
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            return IncomeViewModel(
                Injection.provideRepository(context)
            ) as T
        }
        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {
            return AnalyticsViewModel(
                Injection.provideRepository(context)
            ) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                Injection.provideRepository(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}