package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class IncomePostResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
