package com.capstone.bankit.ui.income;

import static com.capstone.bankit.utils.Constants.expensesTypes;
import static com.capstone.bankit.utils.Constants.incomeTypes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.capstone.bankit.databinding.ActivityIncomeBinding;

import java.util.Calendar;

public class IncomeActivity extends AppCompatActivity {
    private ActivityIncomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.edDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    IncomeActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        binding.edDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                incomeTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        binding.btnSave.setOnClickListener(v -> {
            finish();
        });
    }
}