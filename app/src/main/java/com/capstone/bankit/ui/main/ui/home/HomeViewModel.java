package com.capstone.bankit.ui.main.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.utils.DataHelper;

import java.util.List;

public class HomeViewModel extends ViewModel {
    MutableLiveData<List<TransactionModel>> transactionModelList = new MutableLiveData<>(DataHelper.generateTransactions());
    MutableLiveData<Integer> tabFlag = new MutableLiveData<>(0);
}