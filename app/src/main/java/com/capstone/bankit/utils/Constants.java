package com.capstone.bankit.utils;

import android.graphics.Color;

import com.capstone.bankit.R;
import com.capstone.bankit.data.models.TransactionModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Constants {
    public static final String TRANSACTION_TYPE_PAYROLL = "PAYROLL";
    public static final String TRANSACTION_TYPE_UTILITIES = "UTILITIES";
    public static final String TRANSACTION_TYPE_INSURANCE = "INSURANCE";
    public static final String TRANSACTION_TYPE_TRAVEL = "TRAVEL";
    public static final String TRANSACTION_TYPE_MARKETING = "MARKETING";
    public static final String TRANSACTION_TYPE_SUPPLIES = "SUPPLIES";
    public static final String TRANSACTION_TYPE_RENT = "RENT";
    public static final String TRANSACTION_TYPE_SERVICES = "SERVICES";
    public static final String TRANSACTION_TYPE_MAINTENANCE = "MAINTENANCE";
    public static final String TRANSACTION_TYPE_DEPRECIATION = "DEPRECIATION";
    public static final String TRANSACTION_TYPE_LOAN_PAYMENT = "LOAN PAYMENT";
    public static final String TRANSACTION_TYPE_REVENUES = "REVENUES";
    public static final String TRANSACTION_TYPE_INTEREST = "INTEREST";
    public static final String TRANSACTION_TYPE_RENTAL = "RENTAL";
    public static final String TRANSACTION_TYPE_INVESTMENTS = "INVESTMENTS";
    public static final String TRANSACTION_TYPE_SUBSIDIES = "SUBSIDIES";
    public static final String TRANSACTION_TYPE_OTHERS = "OTHERS";

    public static String[] expensesTypes = {
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
            TRANSACTION_TYPE_OTHERS,
    };

    public static String[] incomeTypes = {
            TRANSACTION_TYPE_REVENUES,
            TRANSACTION_TYPE_INTEREST,
            TRANSACTION_TYPE_RENTAL,
            TRANSACTION_TYPE_INVESTMENTS,
            TRANSACTION_TYPE_SUBSIDIES,
            TRANSACTION_TYPE_OTHERS,
    };

    public static final String FLAG_EXPENSE = "EXPENSE";
    public static final String FLAG_INCOME = "INCOME";

    public static int mapTransactionTypeToIcon(String type) {
        if (type == null) {
            return 0;
        }

        switch (type) {
            case TRANSACTION_TYPE_PAYROLL:
                return R.drawable.ic_payroll;
            case TRANSACTION_TYPE_UTILITIES:
                return R.drawable.ic_utilities;
            case TRANSACTION_TYPE_INSURANCE:
                return R.drawable.ic_insurance;
            case TRANSACTION_TYPE_TRAVEL:
                return R.drawable.ic_travel;
            case TRANSACTION_TYPE_MARKETING:
                return R.drawable.ic_marketing;
            case TRANSACTION_TYPE_SUPPLIES:
                return R.drawable.ic_supplies;
            case TRANSACTION_TYPE_RENT:
                return R.drawable.ic_rent;
            case TRANSACTION_TYPE_SERVICES:
                return R.drawable.ic_services;
            default:
                return 0;
        }
    }

    public static int mapTransactionTypeToColor(String type) {
        if (type == null) {
            return 0;
        }

        switch (type) {
            case TRANSACTION_TYPE_PAYROLL:
                return Color.parseColor("#807EA5AF");
            case TRANSACTION_TYPE_UTILITIES:
                return Color.parseColor("#4D7C83");
            case TRANSACTION_TYPE_INSURANCE:
                return Color.parseColor("#90B2A2");
            case TRANSACTION_TYPE_TRAVEL:
                return Color.parseColor("#E6C69F");
            case TRANSACTION_TYPE_MARKETING:
                return Color.parseColor("#133E87");
            case TRANSACTION_TYPE_SUPPLIES:
                return Color.parseColor("#1D7B83");
            case TRANSACTION_TYPE_RENT:
                return Color.parseColor("#117999");
            case TRANSACTION_TYPE_SERVICES:
                return Color.parseColor("#9912A0");
            default:
                return 0;
        }
    }


    public static String convertLongToDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        return dateFormat.format(new Date(timestamp));
    }

    public static String formatToRupiah(double amount) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return rupiahFormat.format(amount);
    }

    public static List<TransactionModel> filterByFlagExpenseIncome(List<TransactionModel> transactions, String flag) {
        return transactions.stream()
                .filter(transaction -> transaction.getFlagExpenseIncome().equals(flag))
                .collect(Collectors.toList());
    }

    public static String formatDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return sdf.format(calendar.getTime());
    }

    public static boolean isEndDateBeforeStartDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(startYear, startMonth, startDay);

        Calendar endDate = Calendar.getInstance();
        endDate.set(endYear, endMonth, endDay);

        return endDate.before(startDate);
    }
}
