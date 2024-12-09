package com.capstone.bankit.ui.main.analytics

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bankit.R
import com.capstone.bankit.data.models.TransactionModel
import com.capstone.bankit.databinding.FragmentAnalyticsBinding
import com.capstone.bankit.ui.adapter.AnalyticsAdapter
import com.capstone.bankit.ui.customview.PieChartView
import com.capstone.bankit.ui.main.ui.analytics.AnalyticsViewModel
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.Constants.filterByFlagExpenseIncome
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.Constants.mapTransactionTypeToColor
import com.capstone.bankit.utils.DataHelper.generateTransactions
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnalyticsFragment : Fragment() {
    private var binding: FragmentAnalyticsBinding? = null

    private lateinit var analyticsViewModel: AnalyticsViewModel

    private val analyticsAdapter: AnalyticsAdapter = AnalyticsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        analyticsViewModel = ViewModelProvider(this)[AnalyticsViewModel::class.java]

        observeViewModel()

        setViews()

        return binding!!.root
    }

    private fun observeViewModel() {
        analyticsViewModel.analyticsModelList.observe(viewLifecycleOwner) { transactionModels ->
            val slices =
                ArrayList<PieChartView.Slice>()
            val groupedData: MutableMap<String?, Double> =
                HashMap()
            for ((_, type, _, _, amount) in transactionModels) {
                groupedData[type] = groupedData.getOrDefault(type, 0.0) + amount
            }

            val totalAmount = groupedData.values.stream()
                .mapToDouble { obj: Double -> obj }
                .sum()

            val updatedTransactionModels: MutableList<TransactionModel> =
                ArrayList()
            for ((type, amount) in groupedData) {
                val percentage =
                    Math.round((amount / totalAmount) * 100).toInt()
                val color: Int = mapTransactionTypeToColor(type)
                val slice =
                    PieChartView.Slice(
                        type!!,
                        percentage.toFloat(),
                        color
                    )
                slices.add(slice)

                val updatedModel = TransactionModel()
                updatedModel.type = type
                updatedModel.amount = amount
                updatedModel.percentage = percentage
                updatedTransactionModels.add(updatedModel)
            }

            binding!!.pieChart.setData(slices)
            binding!!.tvTotal.text = formatToRupiah(totalAmount)
            analyticsAdapter.submitList(updatedTransactionModels)
        }

        analyticsViewModel.tabFlag.observe(viewLifecycleOwner) { tabFlag ->
            if (tabFlag == 1) {
                binding!!.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding!!.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding!!.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding!!.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
            } else {
                binding!!.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding!!.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding!!.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primaryBlue
                    )
                )
                binding!!.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
        }
    }

    private fun setViews() {
        binding!!.tvDateRange.setOnClickListener {
            datePickerDialog()
        }

        binding!!.rvAnalytics.adapter = analyticsAdapter
        binding!!.rvAnalytics.layoutManager = LinearLayoutManager(requireContext())

        binding!!.btnIncomeTab.setOnClickListener {
            if (analyticsViewModel.tabFlag.getValue() != 1) {
                analyticsViewModel.analyticsModelList.postValue(
                    filterByFlagExpenseIncome(
                        generateTransactions(),
                        Constants.FLAG_INCOME
                    )
                )
                analyticsViewModel.tabFlag.postValue(1)
            }
        }

        binding!!.btnExpenseTab.setOnClickListener {
            if (analyticsViewModel.tabFlag.getValue() != 2) {
                analyticsViewModel.analyticsModelList.postValue(
                    filterByFlagExpenseIncome(
                        generateTransactions(),
                        Constants.FLAG_EXPENSE
                    )
                )
                analyticsViewModel.tabFlag.postValue(2)
            }
        }
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")

        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection: Pair<Long, Long> ->
            val startDate = selection.first
            val endDate = selection.second

            val sdf =
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val startDateString = sdf.format(Date(startDate))
            val endDateString = sdf.format(Date(endDate))

            val selectedDateRange = "$startDateString - $endDateString"
            binding!!.tvDateRange.text = selectedDateRange
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
}