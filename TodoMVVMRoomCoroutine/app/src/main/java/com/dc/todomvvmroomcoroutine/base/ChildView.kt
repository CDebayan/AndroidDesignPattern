package com.dc.todomvvmroomcoroutine.base

import android.view.View

data class ChildView(
        val view: View,
        val title: String? = "",
        val showBack: Boolean = true,
)