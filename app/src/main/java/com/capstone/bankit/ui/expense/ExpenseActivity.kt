package com.capstone.bankit.ui.expense

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.capstone.bankit.R
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.databinding.ActivityExpenseBinding
import com.capstone.bankit.ui.customview.ReceiptBottomSheetFragment
import com.capstone.bankit.ui.expensedetail.ExpenseDetailActivity
import com.capstone.bankit.utils.Constants
import com.capstone.bankit.utils.TokenManager
import com.capstone.bankit.utils.ViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseBinding
    private lateinit var expenseViewModel: ExpenseViewModel
    private var imagePathLocation: String? = null

    private lateinit var token: String
    private lateinit var imageUrl: String
    private var imagePath: String? = null

    private val detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                finish()
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    expenseViewModel.selectedImageFile.postValue(toFile(it)?.let { it1 ->
                        reduceFileSize(
                            it1
                        )
                    })
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val file = File(imagePathLocation ?: return@registerForActivityResult)
                val bmp = rotateImage(BitmapFactory.decodeFile(file.path), imagePathLocation ?: "")
                try {
                    FileOutputStream(file).use { fos ->
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                expenseViewModel.selectedImageFile.postValue(reduceFileSize(file))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        expenseViewModel = ViewModelProvider(this, ViewModelFactory(this))[ExpenseViewModel::class.java]
        token = TokenManager.getInstance(this)?.token.toString()

        observeSelectedImage()
        setListeners()
    }

    private fun observeSelectedImage() {
        expenseViewModel.selectedImageFile.observe(this) { file ->
            if (file != null) {
                binding.layoutReceipt.visibility = View.VISIBLE
                binding.ivReceipt.setImageBitmap(BitmapFactory.decodeFile(file.path))
                expenseViewModel.postReceipt("Bearer $token", file, onFailure = {
                    Toast.makeText(
                        this,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()})

            } else {
                binding.layoutReceipt.visibility = View.GONE
            }
        }

        expenseViewModel.receiptUploadUrl.observe(this) {
            imageUrl = it
        }

        expenseViewModel.receiptUploadPath.observe(this) {
            imagePath = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.edDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year1, monthOfYear, dayOfMonth ->
                    binding.edDate.setText("$dayOfMonth-${monthOfYear + 1}-$year1")
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.expensesTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        val paymentMethodAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.paymentMethods
        )
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = paymentMethodAdapter

        binding.edReceipt.setOnClickListener {
            openReceiptBottomSheet()
        }

        binding.btnAutoSave.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val date = binding.edDate.text.toString()
            val dateFormatted = Constants.convertFormatDate(date)
            val category = binding.spinnerCategory.selectedItem.toString()

            if (title.isEmpty() || date.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Fill the title, date, and category!", Toast.LENGTH_SHORT).show()
            } else if (imagePath.isNullOrEmpty()) {
                Toast.makeText(this, "Upload the receipt first, before process it", Toast.LENGTH_SHORT).show()
            } else {
                expenseViewModel.getReceipt(
                    token = "Bearer $token",
                    filename = imagePath.toString(),
                    onFailure = {
                        Toast.makeText(this, "Failed to process the image", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = { response ->
                        if (response.message == "Receipt not found or still processing"){
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        } else {
                            expenseViewModel.postExpense(
                                token = "Bearer $token",
                                request = ExpenseRequest(
                                    title = title,
                                    date = dateFormatted,
                                    amount = response.data?.extractedData?.totalValue,
                                    tax = response.data?.extractedData?.taxValue,
                                    paymentMethod = response.data?.extractedData?.paymentMethod,
                                    category = category,
                                    receiptUrl = imageUrl,
                                    note = "Set automatically by the AI"
                                ),
                                onFailure = {
                                    Toast.makeText(this, "Failed to post the data", Toast.LENGTH_SHORT).show()
                                },
                                onSuccess = { expense ->
                                    val iDetail = Intent(this, ExpenseDetailActivity::class.java)
                                    iDetail.putExtra("expenseId", expense.data?.id)
                                    iDetail.putExtra("EXTRA_FILE", expenseViewModel.selectedImageFile.value)
                                    detailLauncher.launch(iDetail)
                                }
                            )
                        }
                    }
                )
            }
        }

        binding.btnSave.setOnClickListener {
            if (expenseViewModel.selectedImageFile.value == null) {
                Toast.makeText(this, "Add receipt first!", Toast.LENGTH_SHORT).show()
            } else {
                val title = binding.edTitle.text.toString()
                val date = binding.edDate.text.toString()
                val dateFormatted = Constants.convertFormatDate(date)
                val amount = binding.edAmount.text.toString().toInt()
                val category = binding.spinnerCategory.selectedItem.toString()
                val note = binding.edNote.text.toString()
                val tax = binding.edTax.text.toString().toDoubleOrNull() ?: 0.0
                val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()
                if (title.isEmpty() || date.isEmpty() || category.isEmpty() || note.isEmpty()) {
                    Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                } else {
                    val request = ExpenseRequest(
                        title = title,
                        date = dateFormatted,
                        amount = amount,
                        tax = tax,
                        paymentMethod = paymentMethod,
                        category = category,
                        receiptUrl = imageUrl,
                        note = note
                    )
                    expenseViewModel.postExpense(
                        token = "Bearer $token",
                        request = request,
                        onFailure = {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        },
                        onSuccess = { expense ->
                            val iDetail = Intent(this, ExpenseDetailActivity::class.java)
                            iDetail.putExtra("expenseId", expense.data?.id)
                            iDetail.putExtra("EXTRA_FILE", expenseViewModel.selectedImageFile.value)
                            detailLauncher.launch(iDetail)
                        }
                    )
                }
            }
        }
    }

    private fun openReceiptBottomSheet() {
        val bottomSheetFragment = ReceiptBottomSheetFragment()
        bottomSheetFragment.setOnButtonClickListener(object : ReceiptBottomSheetFragment.OnImagePickerClickListener {
            @SuppressLint("QueryPermissionsNeeded")
            override fun cameraClick() {
                if (checkImagePermission()) {
                    try {
                        val file = File.createTempFile(
                            SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(System.currentTimeMillis()),
                            ".jpg",
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        )
                        imagePathLocation = file.absolutePath
                        val photoUri = FileProvider.getUriForFile(
                            this@ExpenseActivity,
                            applicationContext.packageName,
                            file
                        )
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        cameraLauncher.launch(intent)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        this@ExpenseActivity,
                        REQUIRED_CAMERA_PERMISSION,
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            }

            override fun galleryClick() {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Pick an Image")
                galleryLauncher.launch(chooser)
            }
        })
        bottomSheetFragment.show(supportFragmentManager, "ReceiptBottomSheet")
    }

    private fun rotateImage(bitmap: Bitmap, path: String): Bitmap {
        val matrix = Matrix()
        try {
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.setRotate(90f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.setRotate(180f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun toFile(uri: Uri): File? {
        var tempFile: File? = null
        try {
            tempFile = File.createTempFile(
                "Image_${packageName}_${System.currentTimeMillis()}",
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tempFile
    }

    private fun reduceFileSize(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fos)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun checkImagePermission(): Boolean {
        for (permission in REQUIRED_CAMERA_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    companion object {
        private val REQUIRED_CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 100
    }
}
