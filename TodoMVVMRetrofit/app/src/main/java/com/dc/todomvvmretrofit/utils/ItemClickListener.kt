package com.dc.todomvvmretrofit.utils

interface ItemClickListener {
    fun onItemClick(position: Int,option : String = "")
}