package com.capstone.bankit.ui.income;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.ActivityIncomeBinding;
import com.capstone.bankit.ui.main.MainActivity;

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

        binding.btnSave.setOnClickListener(v -> {
            finish();
        });
    }
}