package com.capstone.bankit.ui.main.home

import android.annotation.SuppressLint
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
import com.capstone.bankit.data.models.Data
import com.capstone.bankit.data.models.DataItem
import com.capstone.bankit.databinding.FragmentHomeBinding
import com.capstone.bankit.ui.adapter.TransactionAdapter
import com.capstone.bankit.ui.auth.AuthActivity
import com.capstone.bankit.ui.expense.ExpenseActivity
import com.capstone.bankit.ui.income.IncomeActivity
import com.capstone.bankit.utils.Constants.formatToRupiah
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAuth: FirebaseAuth

    private val transactionAdapter: TransactionAdapter = TransactionAdapter(0)

    private var expense: List<DataItem?>? = emptyList()
    private var income: List<DataItem?>? = emptyList()
    private var allTransactions: List<DataItem?>? = emptyList()

    private lateinit var token: String
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[HomeViewModel::class.java]

        token = TokenManager.getInstance(requireContext())?.token.toString()
        name = TokenManager.getInstance(requireContext())?.name
        FirebaseApp.initializeApp(requireContext())
        mAuth = FirebaseAuth.getInstance()

        observeViewModel()
        setViews()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        homeViewModel.name.observe(viewLifecycleOwner) { updatedName ->
            name = updatedName
        }

        if (name.isNullOrEmpty()) {
            homeViewModel.getUser(
                "Bearer $token",
                onFailure = {
                    Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                },
                onLoading = {},
                onSuccess = { userResponse ->
                    TokenManager.getInstance(requireContext())?.saveToken(name = userResponse.data?.username, email = userResponse.data?.email, token = token)
                }
            )
        }

        homeViewModel.getExpense(
            token = "Bearer $token",
            onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                TokenManager.getInstance(requireContext())?.clearToken()
                startActivity(Intent(
                    activity, AuthActivity::class.java
                ))
                activity?.finish()
            },
            onLoading = {
//                if (it) {
//                    Toast.makeText(requireContext(), "Still loading", Toast.LENGTH_SHORT).show()
//                }
            }
        )
        homeViewModel.getIncome(
            token = "Bearer $token",
            onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                TokenManager.getInstance(requireContext())?.clearToken()
                startActivity(Intent(
                    activity, AuthActivity::class.java
                ))
                activity?.finish()
            },
            onLoading = {
//                if (it) {
//                    Toast.makeText(requireContext(), "Still loading", Toast.LENGTH_SHORT).show()
//                }
            }
        )
        homeViewModel.getTransaction(
            token = "Bearer $token",
            onFailure = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                TokenManager.getInstance(requireContext())?.clearToken()
                startActivity(Intent(
                    activity, AuthActivity::class.java
                ))
                activity?.finish()
            },
            onLoading = {
//                if (it) {
//                    Toast.makeText(requireContext(), "Still loading", Toast.LENGTH_SHORT).show()
//                }
            }
        )

        homeViewModel.expense.observe(viewLifecycleOwner) { expenseResponse ->
            expenseResponse?.data?.let { expenseData ->
                if (expenseData.isNotEmpty()) {
                    expense = expenseData
                }
            }
            updateBalance(homeViewModel.incomes.value?.data, expenseResponse?.data)
        }

        homeViewModel.incomes.observe(viewLifecycleOwner) { incomeResponse ->
            incomeResponse?.data?.let { incomeData ->
                if (incomeData.isNotEmpty()) {
                    income = incomeData
                }
            }
            updateBalance(incomeResponse?.data, homeViewModel.expense.value?.data)
        }

        homeViewModel.transactions.observe(viewLifecycleOwner) { transactionResponse ->
            transactionResponse?.data?.let { transactionData ->
                if (transactionData.isNotEmpty()) {
                    allTransactions = transactionData
                    transactionAdapter.submitList(allTransactions)
                }
            }
        }

        homeViewModel.tabFlag.observe(viewLifecycleOwner) { tabFlag ->
            // Update the adapter based on selected tab
            if (tabFlag == 1) {
                transactionAdapter.submitList(income)
            } else if (tabFlag == 2) {
                transactionAdapter.submitList(expense)
            } else {
                transactionAdapter.submitList(allTransactions)
            }

            // Update the tab flag in the adapter
            transactionAdapter.updateTabFlag(tabFlag)

            // Update tab styles
            updateTabStyles(tabFlag)
        }

        binding.tvUsername.text = "Hello, $name!"
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

    @SuppressLint("SetTextI18n")
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

        binding.btnRefresh.setOnClickListener {
            homeViewModel.getExpense(
                token = "Bearer $token",
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    TokenManager.getInstance(requireContext())?.clearToken()
                    startActivity(Intent(
                        activity, AuthActivity::class.java
                    ))
                    activity?.finish()
                },
                onLoading = {
                if (it) {
                    Toast.makeText(requireContext(), "Data refreshed", Toast.LENGTH_SHORT).show()
                }
                }
            )
            homeViewModel.getIncome(
                token = "Bearer $token",
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    TokenManager.getInstance(requireContext())?.clearToken()
                    startActivity(Intent(
                        activity, AuthActivity::class.java
                    ))
                    activity?.finish()
                },
                onLoading = {
//                if (it) {
//                    Toast.makeText(requireContext(), "Still loading", Toast.LENGTH_SHORT).show()
//                }
                }
            )
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
                    TokenManager.getInstance(requireContext())?.clearToken()
                    startActivity(Intent(
                        activity, AuthActivity::class.java
                    ))
                    activity?.finish()
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
                    TokenManager.getInstance(requireContext())?.clearToken()
                    startActivity(Intent(
                        activity, AuthActivity::class.java
                    ))
                    activity?.finish()
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
