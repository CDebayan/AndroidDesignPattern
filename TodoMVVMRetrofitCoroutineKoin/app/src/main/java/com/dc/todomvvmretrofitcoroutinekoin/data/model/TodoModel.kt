package com.dc.todomvvmretrofitcoroutinekoin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoModel(
    @SerializedName("todoId")
    val todoId: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("dateTime")
    val dateTime: String? = null,
    @SerializedName("priority")
    val priority: String? = null
): Parcelable
