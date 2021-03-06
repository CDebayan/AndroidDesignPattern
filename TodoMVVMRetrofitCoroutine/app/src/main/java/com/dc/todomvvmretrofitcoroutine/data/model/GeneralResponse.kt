package com.dc.todomvvmretrofitcoroutine.data.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse (
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null
)