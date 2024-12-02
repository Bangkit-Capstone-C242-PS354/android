package com.capstone.bankit.ui.main.ui.analytics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.utils.Constants;
import com.capstone.bankit.utils.DataHelper;

import java.util.List;

public class AnalyticsViewModel extends ViewModel {
    MutableLiveData<List<TransactionModel>> analyticsModelList = new MutableLiveData<>(Constants.filterByFlagExpenseIncome(DataHelper.generateTransactions(), Constants.FLAG_INCOME));
    MutableLiveData<Integer> tabFlag = new MutableLiveData<>(1);
}