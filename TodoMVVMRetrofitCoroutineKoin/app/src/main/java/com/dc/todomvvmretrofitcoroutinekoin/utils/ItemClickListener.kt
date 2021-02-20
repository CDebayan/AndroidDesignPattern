package com.dc.todomvvmretrofitcoroutinekoin.utils

interface ItemClickListener {
    fun onItemClick(position: Int,option : String = "")
}