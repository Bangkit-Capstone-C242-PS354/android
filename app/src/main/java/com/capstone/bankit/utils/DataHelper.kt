package com.capstone.bankit.utils

import com.capstone.bankit.data.models.TransactionModel
import com.capstone.bankit.utils.Constants.FLAG_EXPENSE
import com.capstone.bankit.utils.Constants.FLAG_INCOME
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_INSURANCE
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_MARKETING
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_PAYROLL
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_RENT
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_SERVICES
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_SUPPLIES
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_TRAVEL
import com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_UTILITIES

object DataHelper {
    fun generateTransactions(): List<TransactionModel> {
        val transactions = mutableListOf<TransactionModel>()

        val transactionTypes = arrayOf(
            TRANSACTION_TYPE_PAYROLL,
            TRANSACTION_TYPE_UTILITIES,
            TRANSACTION_TYPE_INSURANCE,
            TRANSACTION_TYPE_TRAVEL,
            TRANSACTION_TYPE_MARKETING,
            TRANSACTION_TYPE_SUPPLIES,
            TRANSACTION_TYPE_RENT,
            TRANSACTION_TYPE_SERVICES
        )

        for (i in 0 until 20) {
            val transaction = TransactionModel().apply {
                id = i + 1
                type = transactionTypes[i % transactionTypes.size]
                date = System.currentTimeMillis() - (i * 86400000L)  // 86400000L = 1 day in milliseconds
                desc = "Description for transaction ${i + 1}"
                amount = (i + 1) * 10000.50
                flagExpenseIncome = if (i % 2 == 0) FLAG_EXPENSE else FLAG_INCOME
            }

            transactions.add(transaction)
        }

        return transactions
    }
}
