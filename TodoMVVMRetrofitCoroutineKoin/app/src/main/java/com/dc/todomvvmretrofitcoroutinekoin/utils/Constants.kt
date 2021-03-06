package com.dc.todomvvmretrofitcoroutinekoin.utils

const val USER_CREDENTIAL: String = "userCredential"

////////////////////////////////////////////////////////////////////

enum class RetrofitInstances {
    NoLogger,
    WithOutAuth,
    WithAuth
}

enum class RecyclerViewOption {
    List,
    Add,
    Edit,
    Delete
}