package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataToken? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataToken(

	@field:SerializedName("customToken")
	val customToken: String? = null
)
