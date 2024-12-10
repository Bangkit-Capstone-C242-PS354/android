package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
