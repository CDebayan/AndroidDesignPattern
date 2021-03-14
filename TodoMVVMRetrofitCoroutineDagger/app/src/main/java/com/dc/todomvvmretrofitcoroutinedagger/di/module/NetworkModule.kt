package com.dc.todomvvmretrofitcoroutinedagger.di.module

import android.app.Application
import com.dc.todomvvmretrofitcoroutinedagger.BuildConfig
import com.dc.todomvvmretrofitcoroutinedagger.data.network.ApiService
import com.dc.todomvvmretrofitcoroutinedagger.data.network.TokenAuthenticator
import com.dc.todomvvmretrofitcoroutinedagger.utils.RetrofitInstances
import com.dc.todomvvmretrofitcoroutinedagger.utils.getToken
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL: String = "http://192.168.0.7:4001/"

@Module
class NetworkModule {
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private val interceptor = HttpLoggingInterceptor()

    private val logInterceptor = if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
        interceptor
    }

    @Provides
    @Singleton
    @Named(RetrofitInstances.NoLogger)
    fun invokeNoLogger(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(RetrofitInstances.WithOutAuth)
    fun invokeWithOutAuth(): ApiService {
        okHttpClientBuilder.addInterceptor(logInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun authenticator(application: Application,@Named(RetrofitInstances.NoLogger) apiService: ApiService): TokenAuthenticator {
        return TokenAuthenticator(application,apiService)
    }

    @Provides
    @Singleton
    @Named(RetrofitInstances.WithAuth)
    fun invokeWithAuth(
        application: Application,
        authenticator: TokenAuthenticator
    ): ApiService {
        okHttpClientBuilder.addInterceptor(
            Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Authorization", "Bearer ${application.getToken()}")
                chain.proceed(newRequest.build())
            })
            .authenticator(authenticator)
            .addInterceptor(logInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }

}