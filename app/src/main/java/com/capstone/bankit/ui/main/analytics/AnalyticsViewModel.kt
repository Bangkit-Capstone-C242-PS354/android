package com.capstone.bankit.ui.main.analytics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.R
import com.capstone.bankit.data.models.ExpenseResponse
import com.capstone.bankit.data.models.IncomeResponse
import com.capstone.bankit.data.repository.BankitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AnalyticsViewModel(
    private val bankitRepository: BankitRepository,
    private val context: Context
) : ViewModel() {

    private val _incomes = MutableLiveData<IncomeResponse>()
    val incomes: LiveData<IncomeResponse> = _incomes

    private val _expenses = MutableLiveData<ExpenseResponse>()
    val expense: LiveData<ExpenseResponse> = _expenses

    val tabFlag = MutableLiveData<Int>()

    private fun showDownloadNotification(fileName: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Download Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Download Complete")
            .setContentText("Transaction data exported to $fileName")
            .setSmallIcon(R.drawable.ic_download)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun exportTransactions(
        token: String,
        onSuccess: (File) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = bankitRepository.exportTransactions(token)
                
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }

                val fileName = "transactions_${System.currentTimeMillis()}.xlsx"
                val file = File(downloadsDir, fileName)

                withContext(Dispatchers.IO) {
                    response.byteStream().use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                
                showDownloadNotification(fileName)
                onSuccess(file)
            } catch (e: Exception) {
                onFailure(e.message ?: "Failed to export transactions")
            }
        }
    }

    fun getIncome(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        onLoading(true)
        viewModelScope.launch {
            try {
                val incomeResponse = bankitRepository.getAllIncomes(token)
                _incomes.value = incomeResponse
                onLoading(false)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getIncome: ${e.message}")
            }
        }
    }

    fun getExpense(
        token: String,
        onFailure: (String) -> Unit,
        onLoading: (Boolean) -> Unit
    ) {
        onLoading(true)
        viewModelScope.launch {
            try {
                val expenseResponse = bankitRepository.getAllExpenses(token)
                _expenses.value = expenseResponse
                onLoading(false)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getExpense: ${e.message}")
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
    ) {
        onLoading(true)
        viewModelScope.launch {
            try {
                val incomeResponse = bankitRepository.getFilteredIncomes(token, period, startDate, endDate)
                _incomes.value = incomeResponse
                onLoading(false)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getFilteredIncome: ${e.message}")
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
    ) {
        onLoading(true)
        viewModelScope.launch {
            try {
                val expenseResponse = bankitRepository.getFilteredExpenses(token, period, startDate, endDate)
                _expenses.value = expenseResponse
                onLoading(false)
            } catch (e: Exception) {
                onLoading(false)
                onFailure(e.message.toString())
                Log.e(TAG, "getFilteredExpense: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "AnalyticsViewModel"
        private const val CHANNEL_ID = "download_notification"
        private const val NOTIFICATION_ID = 1
    }
}