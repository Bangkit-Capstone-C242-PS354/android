package com.capstone.bankit.ui.expensedetail;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.capstone.bankit.R;
import com.capstone.bankit.databinding.ActivityExpenseDetailBinding;

import java.io.File;

public class ExpenseDetailActivity extends AppCompatActivity {
    private ActivityExpenseDetailBinding binding;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageFile = (File) getIntent().getSerializableExtra("EXTRA_FILE");

        setViews();
        setListeners();
    }

    private void setViews() {
        binding.ivReceipt.setImageBitmap(BitmapFactory.decodeFile(imageFile.getPath()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}