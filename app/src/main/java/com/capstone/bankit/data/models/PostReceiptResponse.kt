package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class PostReceiptResponse(

	@field:SerializedName("data")
	val data: DataReceipt? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataReceipt(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
