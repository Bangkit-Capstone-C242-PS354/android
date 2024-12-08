package com.capstone.bankit.ui.expense.data;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/receipts/upload")
    Call<ReceiptResponse> uploadReceipt(
            @Part MultipartBody.Part receipt
    );
}