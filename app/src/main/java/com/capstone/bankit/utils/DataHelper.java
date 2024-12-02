package com.capstone.bankit.utils;

import static com.capstone.bankit.utils.Constants.FLAG_EXPENSE;
import static com.capstone.bankit.utils.Constants.FLAG_INCOME;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_INSURANCE;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_MARKETING;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_PAYROLL;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_RENT;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_SERVICES;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_SUPPLIES;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_TRAVEL;
import static com.capstone.bankit.utils.Constants.TRANSACTION_TYPE_UTILITIES;

import com.capstone.bankit.data.models.TransactionModel;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    public static List<TransactionModel> generateTransactions() {
        List<TransactionModel> transactions = new ArrayList<>();

        String[] transactionTypes = {
                TRANSACTION_TYPE_PAYROLL,
                TRANSACTION_TYPE_UTILITIES,
                TRANSACTION_TYPE_INSURANCE,
                TRANSACTION_TYPE_TRAVEL,
                TRANSACTION_TYPE_MARKETING,
                TRANSACTION_TYPE_SUPPLIES,
                TRANSACTION_TYPE_RENT,
                TRANSACTION_TYPE_SERVICES
        };

        for (int i = 0; i < 20; i++) {
            TransactionModel transaction = new TransactionModel();
            transaction.setId(i + 1);
            transaction.setType(transactionTypes[i % transactionTypes.length]);
            transaction.setDate(System.currentTimeMillis() - (i * 86400000L));
            transaction.setDesc("Description for transaction " + (i + 1));
            transaction.setAmount((i + 1) * 10000.50);
            transaction.setFlagExpenseIncome(i % 2 == 0 ? FLAG_EXPENSE : FLAG_INCOME);

            transactions.add(transaction);
        }

        return transactions;
    }
}
