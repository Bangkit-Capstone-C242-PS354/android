package com.capstone.bankit.ui.expense;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.bankit.data.api.ApiClient;
import com.capstone.bankit.ui.expense.data.ApiService;
import com.capstone.bankit.ui.expense.data.ReceiptResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseViewModel extends ViewModel {
    public MutableLiveData<File> selectedImageFile = new MutableLiveData<>();
    public MutableLiveData<String> uploadResult = new MutableLiveData<>();

    public void uploadReceipt(File file) {
        ApiService receiptService = ApiClient.getInstance().create(ApiService.class);

        // Convert file to MultipartBody.Part
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("receipt", file.getName(), requestFile);

        // Call API
        receiptService.uploadReceipt(body).enqueue(new Callback<ReceiptResponse>() {
            @Override
            public void onResponse(Call<ReceiptResponse> call, Response<ReceiptResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    uploadResult.postValue(response.body().getData().getUrl());
                } else {
                    uploadResult.postValue("Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReceiptResponse> call, Throwable t) {
                uploadResult.postValue("Error: " + t.getMessage());
            }
        });
    }
}
