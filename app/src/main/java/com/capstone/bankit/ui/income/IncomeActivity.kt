package com.capstone.bankit.ui.income

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.data.models.IncomeRequest
import com.capstone.bankit.databinding.ActivityIncomeBinding
import com.capstone.bankit.ui.main.MainActivity
import com.capstone.bankit.utils.Constants.convertFormatDate
import com.capstone.bankit.utils.Constants.incomeTypes
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import com.google.android.material.R.layout
import java.util.Calendar

class IncomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeBinding
    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        incomeViewModel = ViewModelProvider(this, ViewModelFactory(this))[IncomeViewModel::class.java]
        token = TokenManager.getInstance(this)?.token.toString()

        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.edDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this@IncomeActivity,
                { _: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                    binding.edDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year1)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            layout.support_simple_spinner_dropdown_item,
            incomeTypes
        )
        adapter.setDropDownViewResource(layout.support_simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        binding.btnSave.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val date = binding.edDate.text.toString()
            val dateFormatted = convertFormatDate(date)
            val amount = binding.edAmount.text.toString().toInt()
            val category = binding.spinnerCategory.selectedItem.toString()
            val request = IncomeRequest(
                title = title,
                date = dateFormatted,
                amount = amount,
                category = category
            )

            incomeViewModel.postIncome(
                token = "Bearer $token",
                onFailure = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onLoading = {},
                onSuccess = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                request = request
            )
        }
    }
}