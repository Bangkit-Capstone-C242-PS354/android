package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class ExpenseDetailResponse(

	@field:SerializedName("data")
	val data: DataExpensePost? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataExpensePost(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("amount")
	val amount: Double? = null,

	@field:SerializedName("receiptUrl")
	val receiptUrl: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAt? = null,

	@field:SerializedName("tax")
	val tax: Double? = null,

	@field:SerializedName("paymentMethod")
	val paymentMethod: String? = null
)
