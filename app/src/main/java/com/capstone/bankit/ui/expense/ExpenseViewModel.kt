package com.capstone.bankit.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.bankit.data.models.ExpenseDetailResponse
import com.capstone.bankit.data.models.ExpensePostResponse
import com.capstone.bankit.data.models.ExpenseRequest
import com.capstone.bankit.data.models.ReceiptCheckResponse
import com.capstone.bankit.data.repository.BankitRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class ExpenseViewModel(private val bankitRepository: BankitRepository) : ViewModel() {
    var selectedImageFile: MutableLiveData<File> = MutableLiveData()

    private val _receiptUploadUrl = MutableLiveData<String>()
    val receiptUploadUrl: LiveData<String> = _receiptUploadUrl

    private val _receiptUploadPath = MutableLiveData<String>()
    val receiptUploadPath: LiveData<String> = _receiptUploadPath

    private val _expenseDetail = MutableLiveData<ExpenseDetailResponse>()
    val expenseDetail: LiveData<ExpenseDetailResponse> = _expenseDetail

    fun postReceipt(token: String, receiptFile: File, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val requestBody = receiptFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val receiptPart = MultipartBody.Part.createFormData("receipt", receiptFile.name, requestBody)

                val response = bankitRepository.postReceipt(token, receiptPart)

                _receiptUploadUrl.postValue(response.data?.url.toString())
                _receiptUploadPath.postValue(response.data?.filename.toString())
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }

    fun postExpense(
        token: String,
        request: ExpenseRequest,
        onSuccess: (ExpensePostResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = bankitRepository.postExpense(token, request)
                onSuccess(response)
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }

    fun getExpenseDetail(
        token: String,
        expenseId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = bankitRepository.getExpenseDetail(token, expenseId)
                _expenseDetail.value = response
                onSuccess()
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }

    fun getReceipt(
        token: String,
        filename: String,
        onSuccess: (ReceiptCheckResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            try {
                val response = bankitRepository.receiptCheck(token, filename)
                onSuccess(response)
            } catch (e: IOException) {
                onFailure(e.message.toString())
            } catch (e: Exception) {
                onFailure(e.message.toString())
            }
        }
    }

}