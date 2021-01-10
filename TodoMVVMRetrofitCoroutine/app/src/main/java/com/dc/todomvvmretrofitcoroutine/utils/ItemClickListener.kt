package com.dc.todomvvmretrofitcoroutine.utils

interface ItemClickListener {
    fun onItemClick(position: Int,option : String = "")
}