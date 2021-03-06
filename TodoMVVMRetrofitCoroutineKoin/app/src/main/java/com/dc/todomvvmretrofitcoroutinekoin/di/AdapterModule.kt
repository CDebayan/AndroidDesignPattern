package com.dc.todomvvmretrofitcoroutinekoin.di

import com.dc.todomvvmretrofitcoroutinekoin.ui.adapter.TodoListAdapter
import org.koin.dsl.module

val  adapterModule = module {
    factory { TodoListAdapter() }
}