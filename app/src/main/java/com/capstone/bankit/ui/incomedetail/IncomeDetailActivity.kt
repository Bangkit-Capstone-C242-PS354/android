package com.capstone.bankit.ui.incomedetail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.data.models.IncomeDetailResponse
import com.capstone.bankit.databinding.ActivityIncomeDetailBinding
import com.capstone.bankit.ui.income.IncomeViewModel
import com.capstone.bankit.ui.main.MainActivity
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory

class IncomeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeDetailBinding
    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var token: String
    private var incomeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        incomeId = intent.getStringExtra("incomeId")
        
        incomeViewModel = ViewModelProvider(this, ViewModelFactory(this))[IncomeViewModel::class.java]
        token = TokenManager.getInstance(this)?.token.toString()

        setViews()
        setListeners()
    }

    private fun setViews() {
        incomeViewModel.getIncomeDetail(
            token = "Bearer $token",
            incomeId = incomeId.toString(),
            onFailure = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show()
            }
        )

        incomeViewModel.incomeDetail.observe(this) { income ->
            binding.tvTitle.text = income.data?.title
            binding.tvDate.text = income.data?.date
            binding.tvCategory.text = income.data?.category
            binding.tvTotalAmount.text = Constants.formatToRupiah(income.data?.amount?.toDouble() ?: 0.0)
        }
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSave.setOnClickListener {
            setResult(RESULT_OK)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
} 