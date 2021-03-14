package com.dc.todomvvmretrofitcoroutinedagger.di.module

import com.dc.todomvvmretrofitcoroutinedagger.ui.view.AddUpdateTodoFragment
import com.dc.todomvvmretrofitcoroutinedagger.ui.view.LoginFragment
import com.dc.todomvvmretrofitcoroutinedagger.ui.view.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeTodoListFragment(): TodoListFragment

    @ContributesAndroidInjector
    abstract fun contributeAddUpdateTodoFragment(): AddUpdateTodoFragment
}