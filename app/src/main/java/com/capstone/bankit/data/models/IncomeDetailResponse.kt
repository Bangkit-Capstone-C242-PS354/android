package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class IncomeDetailResponse(
    @field:SerializedName("data")
    val data: DataIncomePost? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("statusCode")
    val statusCode: Int? = null
)

data class DataIncomePost(
    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("amount")
    val amount: Int? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: CreatedAt? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: UpdatedAt? = null
) 