package com.dc.todomvvmretrofitcoroutinekoin.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("accessToken")
    val accessToken: String? = "",
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int
)