package com.capstone.bankit.ui.main.ui.analytics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.bankit.data.models.TransactionModel
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.Constants.filterByFlagExpenseIncome
import com.capstone.bankit.utils.DataHelper.generateTransactions

class AnalyticsViewModel : ViewModel() {
    var analyticsModelList: MutableLiveData<List<TransactionModel>> = MutableLiveData(
        filterByFlagExpenseIncome(generateTransactions(), Constants.FLAG_INCOME)
    )
    var tabFlag: MutableLiveData<Int> = MutableLiveData(1)
}