package com.dc.todomvvmretrofitcoroutinekoin.di

import com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel.AddUpdateTodoViewModel
import com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel.LoginViewModel
import com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel.TodoListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginViewModel(loginRepository = get(), application = get())
    }

    viewModel {
        TodoListViewModel(repository= get())
    }

    viewModel {
        AddUpdateTodoViewModel(repository= get())
    }
}