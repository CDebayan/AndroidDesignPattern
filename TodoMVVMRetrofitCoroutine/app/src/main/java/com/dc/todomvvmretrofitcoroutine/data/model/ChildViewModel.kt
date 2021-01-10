package com.dc.todomvvmretrofitcoroutine.data.model

import android.view.View

data class ChildViewModel(
    val view: View,
    val title: String? = "",
    val showBack: Boolean = true,
)