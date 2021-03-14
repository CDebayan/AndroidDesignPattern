package com.dc.todomvvmretrofitcoroutinedagger.utils

const val USER_CREDENTIAL: String = "userCredential"

enum class RecyclerViewOption {
    List,
    Add,
    Edit,
    Delete
}

object RetrofitInstances {
    const val NoLogger = "NoLogger"
    const val WithOutAuth = "WithOutAuth"
    const val WithAuth = "WithAuth"
}