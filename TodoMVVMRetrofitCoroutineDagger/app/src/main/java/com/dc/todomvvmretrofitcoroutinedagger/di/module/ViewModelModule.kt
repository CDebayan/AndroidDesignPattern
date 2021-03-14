package com.dc.todomvvmretrofitcoroutinedagger.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofitcoroutinedagger.base.ViewModelProviderFactory
import com.dc.todomvvmretrofitcoroutinedagger.di.key.ViewModelKey
import com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel.AddUpdateTodoViewModel
import com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel.LoginViewModel
import com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    abstract fun bindTodoListViewModel(viewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddUpdateTodoViewModel::class)
    abstract fun bindAddUpdateTodoViewModel(viewModel: AddUpdateTodoViewModel): ViewModel
}