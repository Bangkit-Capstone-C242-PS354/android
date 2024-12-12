package com.capstone.bankit.data.models

import com.google.gson.annotations.SerializedName


data class ChatResponse(
    val statusCode: Int,
    val message: String,
    val data: ChatData
)

data class ChatData(
    val reply: String
) 