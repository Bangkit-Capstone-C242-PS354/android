package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class ReceiptCheckResponse(

	@field:SerializedName("data")
	val data: DataReceiptRes? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class ExtractedData(

	@field:SerializedName("total_value")
	val totalValue: Int? = null,

	@field:SerializedName("tax_value")
	val taxValue: Int? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null
)

data class DataReceiptRes(

	@field:SerializedName("extracted_data")
	val extractedData: ExtractedData? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("file_name")
	val fileName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
