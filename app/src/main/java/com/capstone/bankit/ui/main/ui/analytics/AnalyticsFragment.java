package com.capstone.bankit.ui.main.ui.analytics;

import static com.capstone.bankit.utils.Constants.formatToRupiah;
import static com.capstone.bankit.utils.Constants.mapTransactionTypeToColor;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.capstone.bankit.R;
import com.capstone.bankit.data.models.TransactionModel;
import com.capstone.bankit.databinding.FragmentAnalyticsBinding;
import com.capstone.bankit.ui.adapters.AnalyticsAdapter;
import com.capstone.bankit.ui.adapters.TransactionsAdapter;
import com.capstone.bankit.ui.customview.PieChartView;
import com.capstone.bankit.ui.main.ui.home.HomeViewModel;
import com.capstone.bankit.utils.Constants;
import com.capstone.bankit.utils.DataHelper;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AnalyticsFragment extends Fragment {

    private FragmentAnalyticsBinding binding;

    private AnalyticsViewModel analyticsViewModel;

    private AnalyticsAdapter analyticsAdapter = new AnalyticsAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false);
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);

        observeViewModel();

        setViews();

        return binding.getRoot();
    }

    private void observeViewModel() {
        analyticsViewModel.analyticsModelList.observe(getViewLifecycleOwner(), transactionModels -> {
            ArrayList<PieChartView.Slice> slices = new ArrayList<>();

            Map<String, Double> groupedData = new HashMap<>();
            for (TransactionModel transaction : transactionModels) {
                groupedData.put(
                        transaction.getType(),
                        groupedData.getOrDefault(transaction.getType(), 0.0) + transaction.getAmount()
                );
            }

            double totalAmount = groupedData.values().stream().mapToDouble(Double::doubleValue).sum();

            List<TransactionModel> updatedTransactionModels = new ArrayList<>();
            for (Map.Entry<String, Double> entry : groupedData.entrySet()) {
                String type = entry.getKey();
                double amount = entry.getValue();

                int percentage = (int) Math.round((amount / totalAmount) * 100);
                int color = mapTransactionTypeToColor(type);
                PieChartView.Slice slice = new PieChartView.Slice(
                        type,
                        (float) percentage,
                        color
                );
                slices.add(slice);

                TransactionModel updatedModel = new TransactionModel();
                updatedModel.setType(type);
                updatedModel.setAmount(amount);
                updatedModel.setPercentage(percentage);
                updatedTransactionModels.add(updatedModel);
            }

            binding.pieChart.setData(slices);
            binding.tvTotal.setText(formatToRupiah(totalAmount));
            analyticsAdapter.submitList(updatedTransactionModels);
        });

        analyticsViewModel.tabFlag.observe(getViewLifecycleOwner(), tabFlag -> {
            if (tabFlag == 1) {
                binding.btnIncomeTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue)));
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.btnExpenseTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
            } else {
                binding.btnIncomeTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)));
                binding.btnIncomeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryBlue));
                binding.btnExpenseTab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryBlue)));
                binding.btnExpenseTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            }
        });
    }

    private void setViews() {
        binding.tvDateRange.setOnClickListener(v -> {
            DatePickerDialog();
        });

        binding.rvAnalytics.setAdapter(analyticsAdapter);
        binding.rvAnalytics.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnIncomeTab.setOnClickListener(v -> {
            if (!Objects.equals(analyticsViewModel.tabFlag.getValue(), 1)) {
                analyticsViewModel.analyticsModelList.postValue(Constants.filterByFlagExpenseIncome(DataHelper.generateTransactions(), Constants.FLAG_INCOME));
                analyticsViewModel.tabFlag.postValue(1);
            }
        });

        binding.btnExpenseTab.setOnClickListener(v -> {
            if (!Objects.equals(analyticsViewModel.tabFlag.getValue(), 2)) {
                analyticsViewModel.analyticsModelList.postValue(Constants.filterByFlagExpenseIncome(DataHelper.generateTransactions(), Constants.FLAG_EXPENSE));
                analyticsViewModel.tabFlag.postValue(2);
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
}