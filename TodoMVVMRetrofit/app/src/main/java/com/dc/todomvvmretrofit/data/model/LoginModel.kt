package com.dc.todomvvmretrofit.data.model

import com.google.gson.annotations.SerializedName

data class LoginModel (
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("accessToken")
    val accessToken: String? = null,

    @SerializedName("message")
    val message: String? = null
)