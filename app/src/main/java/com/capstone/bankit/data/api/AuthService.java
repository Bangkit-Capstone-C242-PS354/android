package com.capstone.bankit.data.api;

import com.capstone.bankit.data.models.LoginRequest;
import com.capstone.bankit.data.models.LoginResponse;
import com.capstone.bankit.data.models.RegisterRequest;
import com.capstone.bankit.data.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth/signup")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("/auth/signin")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}