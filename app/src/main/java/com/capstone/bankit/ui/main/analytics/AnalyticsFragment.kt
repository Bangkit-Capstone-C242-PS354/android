package com.capstone.bankit.ui.main.analytics

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bankit.R
import com.capstone.bankit.data.models.DataItem
import com.capstone.bankit.databinding.FragmentAnalyticsBinding
import com.capstone.bankit.ui.adapter.AnalyticsAdapter
import com.capstone.bankit.ui.customview.PieChartView
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.Constants.mapTransactionTypeToColor
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.res.ColorStateList
import android.os.Build

class AnalyticsFragment : Fragment() {
    private var binding: FragmentAnalyticsBinding? = null
    private lateinit var analyticsViewModel: AnalyticsViewModel
    private lateinit var token: String

    private val analyticsAdapter: AnalyticsAdapter = AnalyticsAdapter()

    private var expense: List<DataItem?>? = emptyList()
    private var income: List<DataItem?>? = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        token = TokenManager.getInstance(requireContext())?.token.toString()
        analyticsViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[AnalyticsViewModel::class.java]

        observeViewModel()
        setViews()

        // Set initial tab to Income
        analyticsViewModel.tabFlag.postValue(1) // Default to income tab

        // Load data initially as "income" when fragment is first loaded
        analyticsViewModel.getIncome(
            token = "Bearer $token",
            onFailure = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            },
            onLoading = {}
        )
        analyticsViewModel.getExpense(
            token = "Bearer $token",
            onFailure = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            },
            onLoading = {}
        )

        return binding!!.root
    }

    private fun observeViewModel() {
        // Observe income data
        analyticsViewModel.incomes.observe(viewLifecycleOwner) { incomeResponse ->
            if (incomeResponse != null && incomeResponse.data?.isNotEmpty() == true) {
                income = incomeResponse.data
                income?.forEach {
                    Log.d("AnalyticsFragment", "Income Category: ${it?.category}, Amount: ${it?.amount}")
                }
                if (analyticsViewModel.tabFlag.value == 1) {
                    updateAnalyticsView(income, Constants.FLAG_INCOME) // Update pie chart directly for income
                }
            }
        }

        // Observe expense data
        analyticsViewModel.expense.observe(viewLifecycleOwner) { expenseResponse ->
            if (expenseResponse != null && expenseResponse.data?.isNotEmpty() == true) {
                expense = expenseResponse.data
                expense?.forEach {
                    Log.d("AnalyticsFragment", "Expense Category: ${it?.category}, Amount: ${it?.amount}")
                }
                if (analyticsViewModel.tabFlag.value == 2) {
                    updateAnalyticsView(expense, Constants.FLAG_EXPENSE) // Update pie chart directly for expense
                }
            }
        }

        // Observe tab flag changes (to update tabs)
        analyticsViewModel.tabFlag.observe(viewLifecycleOwner) { tabFlag ->
            if (tabFlag == 1) {
                binding!!.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )
                binding!!.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding!!.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding!!.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )

                updateAnalyticsView(income, Constants.FLAG_INCOME)
            } else {
                binding!!.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding!!.btnIncomeTab.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )
                binding!!.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )
                binding!!.btnExpenseTab.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )

                updateAnalyticsView(expense, Constants.FLAG_EXPENSE)
            }
        }
    }

    private fun updateBalance(data: List<DataItem?>?) {
        val total = data?.sumOf { it?.amount ?: 0.0 } ?: 0.0
        binding!!.tvTotal.text = formatToRupiah(total)
    }

    private fun setViews() {
        binding!!.tvDateRange.setOnClickListener { datePickerDialog() }

        binding!!.rvAnalytics.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvAnalytics.adapter = analyticsAdapter

        binding!!.btnIncomeTab.setOnClickListener {
            if (analyticsViewModel.tabFlag.value != 1) {
                analyticsViewModel.tabFlag.postValue(1)
                updateAnalyticsView(income, Constants.FLAG_INCOME)
            }
        }

        binding!!.btnExpenseTab.setOnClickListener {
            if (analyticsViewModel.tabFlag.value != 2) {
                analyticsViewModel.tabFlag.postValue(2)
                updateAnalyticsView(expense, Constants.FLAG_EXPENSE)
            }
        }

        binding!!.btnExport.setOnClickListener {
            checkAndRequestPermissions()
            
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                analyticsViewModel.exportTransactions(
                    token = "Bearer $token",
                    onSuccess = { file ->
                        Toast.makeText(
                            requireContext(),
                            "File exported successfully to Downloads folder: ${file.name}",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onFailure = { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun updateAnalyticsView(data: List<DataItem?>?, flag: String) {
        val nonNullData = data?.filterNotNull()

        if (nonNullData.isNullOrEmpty()) return

        val slices = ArrayList<PieChartView.Slice>()
        val groupedData: MutableMap<String?, Double> = HashMap()

        // Group data by category and sum the amounts
        for (item in nonNullData) {
            val type = item.category
            val amount = item.amount ?: 0.0
            groupedData[type] = groupedData.getOrDefault(type, 0.0) + amount
        }

        val totalAmount = groupedData.values.sum()

        // Create Pie Chart slices
        for ((type, amount) in groupedData) {
            val percentage = Math.round((amount / totalAmount) * 100).toInt()
            val color = mapTransactionTypeToColor(type)
            val slice = PieChartView.Slice(type!!, percentage.toFloat(), color)
            slices.add(slice)
        }

        binding!!.pieChart.setData(slices)
        binding!!.tvTotal.text = formatToRupiah(totalAmount)

        // Update the recycler view with grouped data
        val updatedData = groupedData.map {
            println("Key: ${it.key}, Value: ${it.value}")
            DataItem(category = it.key, amount = it.value)
        }

        analyticsAdapter.submitList(updatedData)
        analyticsAdapter.setTotalAmount(totalAmount)
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")
        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selection: Pair<Long, Long>? ->
            val startDate = selection?.first
            val endDate = selection?.second

            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val startDateString = startDate?.let { Date(it) }?.let { sdf.format(it) }
            val endDateString = endDate?.let { Date(it) }?.let { sdf.format(it) }

            binding!!.tvDateRange.text = "$startDateString - $endDateString"

            val sdfFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateFormat = startDate?.let { Date(it) }?.let { sdfFormat.format(it) }
            val endDateFormat = endDate?.let { Date(it) }?.let { sdfFormat.format(it) }

            // Fetch filtered income and expense data
            analyticsViewModel.getFilteredIncome(
                token = "Bearer $token",
                period = "custom",
                startDate = startDateFormat.toString(),
                endDate = endDateFormat.toString(),
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                onLoading = { if (it) income = emptyList() }
            )

            analyticsViewModel.getFilteredExpense(
                token = "Bearer $token",
                period = "custom",
                startDate = startDateFormat.toString(),
                endDate = endDateFormat.toString(),
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                onLoading = { if (it) expense = emptyList() }
            )
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        
        // Add notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
    }
}
