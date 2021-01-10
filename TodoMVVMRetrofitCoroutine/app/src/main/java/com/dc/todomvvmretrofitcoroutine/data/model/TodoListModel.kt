package com.dc.todomvvmretrofitcoroutine.data.model

import com.google.gson.annotations.SerializedName

data class TodoListModel (
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: List<TodoModel>? = null
)