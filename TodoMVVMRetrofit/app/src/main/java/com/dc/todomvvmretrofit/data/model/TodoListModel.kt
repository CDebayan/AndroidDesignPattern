package com.dc.todomvvmretrofit.data.model

import com.google.gson.annotations.SerializedName

data class TodoListModel (
    @SerializedName("status")
    val status: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: List<TodoModel>? = null
)