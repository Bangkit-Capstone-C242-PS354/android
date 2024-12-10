package com.capstone.bankit.ui.expensedetail

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.bankit.R
import com.capstone.bankit.databinding.ActivityExpenseDetailBinding
import com.capstone.bankit.ui.expense.ExpenseViewModel
import com.capstone.bankit.ui.income.IncomeViewModel
import com.capstone.bankit.ui.main.MainActivity
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import java.io.File

class ExpenseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseDetailBinding
    private var imageFile: File? = null

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var token: String
    private var expenseId: String? = null
    private var navigate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageFile = intent.getSerializableExtra("EXTRA_FILE") as File?
        expenseId = intent.getStringExtra("expenseId")
        navigate = intent.getStringExtra("navigate")


        expenseViewModel = ViewModelProvider(this, ViewModelFactory(this))[ExpenseViewModel::class.java]
        token = TokenManager.getInstance(this)?.token.toString()

        setViews()
        setListeners()
    }

    private fun setViews() {
        expenseViewModel.getExpenseDetail(
            token = "Bearer $token",
            expenseId = expenseId.toString(),
            onFailure = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show()
            }
        )

        expenseViewModel.expenseDetail.observe(this) { expense ->
            binding.tvDate.text = expense.data?.date
            binding.tvNote.text = expense.data?.note
            binding.tvTitle.text = expense.data?.title
            binding.tvCategory.text = expense.data?.category
            binding.tvAmount.text = Constants.formatToRupiah(expense.data?.amount ?: 0.0)
            if (navigate != "INCOME"){
                val receiptUrl = expense.data?.receipt

                Glide.with(this)
                    .load(receiptUrl)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .into(binding.ivReceipt)
            }
        }
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSave.setOnClickListener {
            setResult(RESULT_OK)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if (navigate == "INCOME") {
            binding.ivReceipt.visibility = View.GONE
        }
    }
}