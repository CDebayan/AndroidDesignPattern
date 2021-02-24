package com.dc.todomvvmretrofitcoroutinekoin.di

import com.dc.todomvvmretrofitcoroutinekoin.data.repository.LoginRepository
import com.dc.todomvvmretrofitcoroutinekoin.data.repository.TodoRepository
import com.dc.todomvvmretrofitcoroutinekoin.utils.RetrofitInstances
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single {
        LoginRepository(apiService = get(named(RetrofitInstances.WithOutAuth)))
    }

    single {
        TodoRepository(apiService = get(named(RetrofitInstances.WithAuth)))
    }
}