package com.dc.todomvvmretrofitcoroutinekoin.base

import com.google.gson.annotations.SerializedName

data class GeneralResponse (
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null
)