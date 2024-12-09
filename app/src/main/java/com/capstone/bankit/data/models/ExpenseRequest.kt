package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class ExpenseRequest(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("receipt")
	val receipt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
