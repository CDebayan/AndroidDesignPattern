package com.dc.todomvvmretrofitcoroutinekoin

import android.app.Application
import com.dc.todomvvmretrofitcoroutinekoin.di.adapterModule
import com.dc.todomvvmretrofitcoroutinekoin.di.networkModule
import com.dc.todomvvmretrofitcoroutinekoin.di.repositoryModule
import com.dc.todomvvmretrofitcoroutinekoin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                networkModule,
                viewModelModule,
                repositoryModule,
                adapterModule
            )
        }
    }
}