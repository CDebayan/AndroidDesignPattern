package com.dc.todomvvmretrofitcoroutinedagger.di.component

import android.app.Application
import com.dc.todomvvmretrofitcoroutinedagger.App
import com.dc.todomvvmretrofitcoroutinedagger.di.module.FragmentModule
import com.dc.todomvvmretrofitcoroutinedagger.di.module.NetworkModule
import com.dc.todomvvmretrofitcoroutinedagger.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}