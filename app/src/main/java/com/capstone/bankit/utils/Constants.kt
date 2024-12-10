package com.capstone.bankit.utils

import android.graphics.Color
import com.capstone.bankit.R
import com.capstone.bankit.data.models.TransactionModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Constants {

    const val TRANSACTION_TYPE_PAYROLL = "PAYROLL"
    const val TRANSACTION_TYPE_UTILITIES = "UTILITIES"
    const val TRANSACTION_TYPE_INSURANCE = "INSURANCE"
    const val TRANSACTION_TYPE_TRAVEL = "TRAVEL"
    const val TRANSACTION_TYPE_MARKETING = "MARKETING"
    const val TRANSACTION_TYPE_SUPPLIES = "SUPPLIES"
    const val TRANSACTION_TYPE_RENT = "RENT"
    const val TRANSACTION_TYPE_SERVICES = "SERVICES"
    const val TRANSACTION_TYPE_MAINTENANCE = "MAINTENANCE"
    const val TRANSACTION_TYPE_DEPRECIATION = "DEPRECIATION"
    const val TRANSACTION_TYPE_LOAN_PAYMENT = "LOAN PAYMENT"
    const val TRANSACTION_TYPE_REVENUES = "REVENUES"
    const val TRANSACTION_TYPE_INTEREST = "INTEREST"
    const val TRANSACTION_TYPE_RENTAL = "RENTAL"
    const val TRANSACTION_TYPE_INVESTMENTS = "INVESTMENTS"
    const val TRANSACTION_TYPE_SUBSIDIES = "SUBSIDIES"
    const val TRANSACTION_TYPE_OTHERS = "OTHERS"

    val expensesTypes = arrayOf(
        TRANSACTION_TYPE_PAYROLL,
        TRANSACTION_TYPE_UTILITIES,
        TRANSACTION_TYPE_INSURANCE,
        TRANSACTION_TYPE_TRAVEL,
        TRANSACTION_TYPE_MARKETING,
        TRANSACTION_TYPE_SUPPLIES,
        TRANSACTION_TYPE_RENT,
        TRANSACTION_TYPE_SERVICES,
        TRANSACTION_TYPE_MAINTENANCE,
        TRANSACTION_TYPE_DEPRECIATION,
        TRANSACTION_TYPE_LOAN_PAYMENT,
        TRANSACTION_TYPE_OTHERS
    )

    val paymentMethods = arrayOf(
        "Cash",
        "Debit Card",
        "Credit Card",
        "Bank Transfer",
        "E-Wallet",
        "Other"
    )

    val incomeTypes = arrayOf(
        TRANSACTION_TYPE_REVENUES,
        TRANSACTION_TYPE_INTEREST,
        TRANSACTION_TYPE_RENTAL,
        TRANSACTION_TYPE_INVESTMENTS,
        TRANSACTION_TYPE_SUBSIDIES,
        TRANSACTION_TYPE_OTHERS
    )

    const val FLAG_EXPENSE = "EXPENSE"
    const val FLAG_INCOME = "INCOME"

    fun mapTransactionTypeToIcon(type: String?): Int {
        return when (type) {
            TRANSACTION_TYPE_PAYROLL -> R.drawable.ic_payroll
            TRANSACTION_TYPE_UTILITIES -> R.drawable.ic_utilities
            TRANSACTION_TYPE_INSURANCE -> R.drawable.ic_insurance
            TRANSACTION_TYPE_TRAVEL -> R.drawable.ic_travel
            TRANSACTION_TYPE_MARKETING -> R.drawable.ic_marketing
            TRANSACTION_TYPE_SUPPLIES -> R.drawable.ic_supplies
            TRANSACTION_TYPE_RENT -> R.drawable.ic_rent
            TRANSACTION_TYPE_SERVICES -> R.drawable.ic_services
            TRANSACTION_TYPE_MAINTENANCE -> R.drawable.ic_services
            TRANSACTION_TYPE_DEPRECIATION -> R.drawable.ic_down
            TRANSACTION_TYPE_LOAN_PAYMENT -> R.drawable.ic_payroll
            TRANSACTION_TYPE_REVENUES -> R.drawable.ic_payroll
            TRANSACTION_TYPE_INTEREST -> R.drawable.baseline_interests_24
            TRANSACTION_TYPE_RENTAL -> R.drawable.ic_rent
            TRANSACTION_TYPE_INVESTMENTS -> R.drawable.ic_marketing
            TRANSACTION_TYPE_SUBSIDIES -> R.drawable.ic_insurance
            TRANSACTION_TYPE_OTHERS -> R.drawable.ic_utilities
            else -> R.drawable.ic_supplies
        }
    }

    fun mapTransactionTypeToColor(type: String?): Int {
        return when (type) {
            TRANSACTION_TYPE_PAYROLL -> Color.parseColor("#807EA5AF")
            TRANSACTION_TYPE_UTILITIES -> Color.parseColor("#4D7C83")
            TRANSACTION_TYPE_INSURANCE -> Color.parseColor("#90B2A2")
            TRANSACTION_TYPE_TRAVEL -> Color.parseColor("#E6C69F")
            TRANSACTION_TYPE_MARKETING -> Color.parseColor("#133E87")
            TRANSACTION_TYPE_SUPPLIES -> Color.parseColor("#1D7B83")
            TRANSACTION_TYPE_RENT -> Color.parseColor("#117999")
            TRANSACTION_TYPE_SERVICES -> Color.parseColor("#9912A0")
            TRANSACTION_TYPE_MAINTENANCE -> Color.parseColor("#F3A683")
            TRANSACTION_TYPE_DEPRECIATION -> Color.parseColor("#596275")
            TRANSACTION_TYPE_LOAN_PAYMENT -> Color.parseColor("#A29BFE")
            TRANSACTION_TYPE_REVENUES -> Color.parseColor("#6AB04C")
            TRANSACTION_TYPE_INTEREST -> Color.parseColor("#F7D794")
            TRANSACTION_TYPE_RENTAL -> Color.parseColor("#E17055")
            TRANSACTION_TYPE_INVESTMENTS -> Color.parseColor("#00CEC9")
            TRANSACTION_TYPE_SUBSIDIES -> Color.parseColor("#81ECEC")
            TRANSACTION_TYPE_OTHERS -> Color.parseColor("#D63031")
            else -> Color.parseColor("#FF2521")
        }
    }


    fun convertLongToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        return dateFormat.format(Date(timestamp))
    }

    fun convertDate(date: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        try {
            val parsedDate: Date = date?.let { inputFormat.parse(it) } ?: return ""

            return outputFormat.format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun convertFormatDate(date: String?): String {
        val inputFormat = SimpleDateFormat("d-M-yyyy", Locale.ENGLISH)  // Use d-M-yyyy for flexible day and month
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        try {
            val parsedDate: Date = date?.let { inputFormat.parse(it) } ?: return ""

            return outputFormat.format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun formatToRupiah(amount: Double): String {
        val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return rupiahFormat.format(amount)
    }

    fun filterByFlagExpenseIncome(transactions: List<TransactionModel>, flag: String): List<TransactionModel> {
        return transactions.filter { it.flagExpenseIncome == flag }
    }

    fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        return sdf.format(calendar.time)
    }

    fun isEndDateBeforeStartDate(
        startYear: Int, startMonth: Int, startDay: Int,
        endYear: Int, endMonth: Int, endDay: Int
    ): Boolean {
        val startDate = Calendar.getInstance().apply {
            set(startYear, startMonth, startDay)
        }

        val endDate = Calendar.getInstance().apply {
            set(endYear, endMonth, endDay)
        }

        return endDate.before(startDate)
    }
}
