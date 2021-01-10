package com.dc.todomvvmretrofit.data.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse (
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null
)