package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("data")
	val data: DataUserUpdate? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class DataUserUpdate(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("totalIncome")
	val totalIncome: Int? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("totalBalance")
	val totalBalance: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("totalExpense")
	val totalExpense: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAt? = null
)

data class UpdateUserRequest (
	val username: String
)
