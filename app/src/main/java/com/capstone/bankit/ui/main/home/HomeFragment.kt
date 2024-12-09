package com.capstone.bankit.ui.main.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bankit.R
import com.capstone.bankit.data.models.DataItem
import com.capstone.bankit.databinding.FragmentHomeBinding
import com.capstone.bankit.ui.adapter.TransactionAdapter
import com.capstone.bankit.ui.expense.ExpenseActivity
import com.capstone.bankit.ui.income.IncomeActivity
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private val transactionAdapter: TransactionAdapter = TransactionAdapter(0)

    private var expense: List<DataItem?>? = emptyList()
    private var income: List<DataItem?>? = emptyList()

    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[HomeViewModel::class.java]

        token = TokenManager.getInstance(requireContext())?.token.toString()

        observeViewModel()
        setViews()

        return binding.root
    }

    private fun observeViewModel() {
        homeViewModel.getExpense(
            token = "Bearer $token",
            onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            onLoading = {
                showLoading(it)
            }
        )
        homeViewModel.getIncome(
            token = "Bearer $token",
            onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            onLoading = {
                showLoading(it)
            }
        )

        homeViewModel.expense.observe(viewLifecycleOwner) { expenseResponse ->
            if (expenseResponse != null && expenseResponse.data?.isNotEmpty() == true) {
                expense = expenseResponse.data
            }
            updateBalance(homeViewModel.incomes.value?.data, expenseResponse.data)
        }

        homeViewModel.incomes.observe(viewLifecycleOwner) { incomeResponse ->
            if (incomeResponse != null && incomeResponse.data?.isNotEmpty() == true) {
                income = incomeResponse.data
                // Update the adapter list initially with income data
                transactionAdapter.submitList(incomeResponse.data)
            }
            updateBalance(incomeResponse.data, homeViewModel.expense.value?.data)
        }

        homeViewModel.tabFlag.observe(viewLifecycleOwner) { tabFlag ->
            // Update the adapter based on selected tab
            if (tabFlag == 1) {
                transactionAdapter.submitList(income)
            } else if (tabFlag == 2) {
                transactionAdapter.submitList(expense)
            }

            // Update the tab flag in the adapter
            transactionAdapter.updateTabFlag(tabFlag)

            // Update tab styles
            updateTabStyles(tabFlag)
        }
    }

    private fun showLoading(isLoadng: Boolean) {
        //
    }

    private fun updateTabStyles(tabFlag: Int) {
        when (tabFlag) {
            1 -> {
                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
            }
            2 -> {
                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.primaryBlue)
                )
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            else -> {
                binding.btnIncomeTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
                binding.btnExpenseTab.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue))
            }
        }
    }

    private fun setViews() {
        binding.rvTransactions.adapter = transactionAdapter
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        binding.btnExpenses.setOnClickListener {
            startActivity(Intent(requireContext(), ExpenseActivity::class.java))
        }

        binding.btnIncomes.setOnClickListener {
            startActivity(Intent(requireContext(), IncomeActivity::class.java))
        }

        binding.tvDateRange.setOnClickListener {
            datePickerDialog()
        }

        binding.btnIncomeTab.setOnClickListener {
            if (homeViewModel.tabFlag.value == 1) {
                homeViewModel.tabFlag.postValue(0)
            } else {
                homeViewModel.tabFlag.postValue(1)
            }
        }

        binding.btnExpenseTab.setOnClickListener {
            if (homeViewModel.tabFlag.value == 2) {
                homeViewModel.tabFlag.postValue(0)
            } else {
                homeViewModel.tabFlag.postValue(2)
            }
        }
    }

    private fun updateBalance(incomeList: List<DataItem?>?, expenseList: List<DataItem?>?) {
        val totalIncome = incomeList?.sumOf { it?.amount ?: 0.0 } ?: 0.0
        val totalExpense = expenseList?.sumOf { it?.amount ?: 0.0 } ?: 0.0

        val total = formatToRupiah((totalIncome - totalExpense))

        binding.tvBalance.text = total
    }

    private fun datePickerDialog() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")

        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection: Pair<Long, Long> ->
            val startDate = selection.first
            val endDate = selection.second

            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val startDateString = sdf.format(Date(startDate))
            val endDateString = sdf.format(Date(endDate))

            val sdfFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateFormat = sdfFormat.format(Date(startDate))
            val endDateFormat = sdfFormat.format(Date(endDate))

            val selectedDateRange = "$startDateString - $endDateString"
            binding.tvDateRange.text = selectedDateRange

            homeViewModel.getFilteredIncome(
                token = "Bearer $token",
                period = "custom",
                startDate = startDateFormat,
                endDate = endDateFormat,
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                onLoading = {
                    if (it) {
                        income = emptyList()
                    }
                }
            )

            homeViewModel.getFilteredExpense(
                token = "Bearer $token",
                period = "custom",
                startDate = startDateFormat,
                endDate = endDateFormat,
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                },
                onLoading = {
                    if (it) {
                        expense = emptyList()
                    }
                }
            )
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }
}
