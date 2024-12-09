package com.capstone.bankit.ui.expensedetail

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.bankit.databinding.ActivityExpenseDetailBinding
import java.io.File

class ExpenseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseDetailBinding
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageFile = intent.getSerializableExtra("EXTRA_FILE") as File?

        setViews()
        setListeners()
    }

    private fun setViews() {
        binding.ivReceipt.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.path))
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnSave.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}