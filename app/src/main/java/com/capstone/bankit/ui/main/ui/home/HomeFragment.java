package com.capstone.bankit.ui.main.ui.home;

import static com.capstone.bankit.utils.Constants.formatDate;
import static com.capstone.bankit.utils.Constants.isEndDateBeforeStartDate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.capstone.bankit.R;
import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.databinding.FragmentHomeBinding;
import com.capstone.bankit.ui.adapters.TransactionsAdapter;
import com.capstone.bankit.ui.expense.ExpenseActivity;
import com.capstone.bankit.ui.income.IncomeActivity;
import com.capstone.bankit.utils.Constants;
import com.capstone.bankit.utils.DataHelper;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private TransactionsAdapter transactionsAdapter = new TransactionsAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        observeViewModel();
        setViews();

        return binding.getRoot();
    }

    private void observeViewModel() {
        homeViewModel.transactionModelList.observe(getViewLifecycleOwner(), transactionModels -> {
            transactionsAdapter.submitList(transactionModels);
        });

        homeViewModel.tabFlag.observe(getViewLifecycleOwner(), tabFlag -> {
            if (tabFlag == 1) {
                binding.btnIncomeTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue)));
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.btnExpenseTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
            } else if (tabFlag == 2) {
                binding.btnIncomeTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
                binding.btnExpenseTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue)));
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            } else {
                binding.btnIncomeTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
                binding.btnExpenseTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
            }
        });
    }

    private void setViews() {
        List<TransactionModel> transactionModelList = DataHelper.generateTransactions();
        transactionsAdapter.submitList(transactionModelList);

        binding.rvTransactions.setAdapter(transactionsAdapter);
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnExpenses.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ExpenseActivity.class));
        });

        binding.btnIncomes.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), IncomeActivity.class));
        });

        binding.tvDateRange.setOnClickListener(v -> {
            DatePickerDialog();
        });

        binding.btnIncomeTab.setOnClickListener(v -> {
            if (Objects.equals(homeViewModel.tabFlag.getValue(), 1)) {
                homeViewModel.transactionModelList.postValue(DataHelper.generateTransactions());
                homeViewModel.tabFlag.postValue(0);
            } else {
                homeViewModel.transactionModelList.postValue(Constants.filterByFlagExpenseIncome(DataHelper.generateTransactions(), Constants.FLAG_INCOME));
                homeViewModel.tabFlag.postValue(1);
            }
        });

        binding.btnExpenseTab.setOnClickListener(v -> {
            if (Objects.equals(homeViewModel.tabFlag.getValue(), 2)) {
                homeViewModel.transactionModelList.postValue(DataHelper.generateTransactions());
                homeViewModel.tabFlag.postValue(0);
            } else {
                homeViewModel.transactionModelList.postValue(Constants.filterByFlagExpenseIncome(DataHelper.generateTransactions(), Constants.FLAG_EXPENSE));
                homeViewModel.tabFlag.postValue(2);
            }
        });
    }

    private void DatePickerDialog() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {

            Long startDate = selection.first;
            Long endDate = selection.second;

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            String selectedDateRange = startDateString + " - " + endDateString;

            binding.tvDateRange.setText(selectedDateRange);
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}