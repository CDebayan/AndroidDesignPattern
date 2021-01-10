package com.dc.todomvvmretrofitcoroutine.data.model

import com.google.gson.annotations.SerializedName

data class AddTodoModel (
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: TodoModel? = null
)