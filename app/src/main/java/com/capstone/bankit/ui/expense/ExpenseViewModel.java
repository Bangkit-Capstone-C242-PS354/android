package com.capstone.bankit.ui.expense;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.utils.Constants;
import com.capstone.bankit.utils.DataHelper;

import java.io.File;
import java.util.List;

public class ExpenseViewModel extends ViewModel {
    MutableLiveData<File> selectedImageFile = new MutableLiveData<>();
}