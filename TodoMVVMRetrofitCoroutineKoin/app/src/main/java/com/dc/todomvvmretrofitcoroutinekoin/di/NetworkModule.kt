package com.dc.todomvvmretrofitcoroutinekoin.di

import com.dc.todomvvmretrofitcoroutinekoin.data.network.RetrofitClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named("withoutAuth")) {
        RetrofitClient.invokeWithOutAuth()
    }

    single(named("withAuth")) {
        RetrofitClient.invokeWithAuth(context = get())
    }
}