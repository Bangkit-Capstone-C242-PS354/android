package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: DataRegister? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataRegister(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
