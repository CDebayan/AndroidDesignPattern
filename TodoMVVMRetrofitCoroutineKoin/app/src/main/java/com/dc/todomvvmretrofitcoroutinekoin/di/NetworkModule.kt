package com.dc.todomvvmretrofitcoroutinekoin.di

import android.content.Context
import android.util.Log
import com.dc.todomvvmretrofitcoroutinekoin.BuildConfig
import com.dc.todomvvmretrofitcoroutinekoin.data.network.ApiService
import com.dc.todomvvmretrofitcoroutinekoin.data.network.TokenAuthenticator
import com.dc.todomvvmretrofitcoroutinekoin.utils.RetrofitInstances
import com.dc.todomvvmretrofitcoroutinekoin.utils.getToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

val networkModule = module {
    single{
        TokenAuthenticator(context = get(), apiService = get(named(RetrofitInstances.NoLogger)))
    }

    single(named(RetrofitInstances.NoLogger)) {
        invokeNoLogger()
    }

    single(named(RetrofitInstances.WithOutAuth)) {
        invokeWithOutAuth()
    }

    single(named(RetrofitInstances.WithAuth)) {
        invokeWithAuth(context = get(),authenticator = get())
    }
}

private const val BASE_URL: String = "http://192.168.0.7:4001/"

private val okHttpClientBuilder = OkHttpClient.Builder()

private val logInterceptor = if (BuildConfig.DEBUG) {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    interceptor
} else {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.NONE
    interceptor
}

private fun invokeNoLogger(): ApiService {
    Log.d("logInterceptor", logInterceptor.toString())
    Log.d("logInterceptor", okHttpClientBuilder.toString())
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}

private fun invokeWithOutAuth(): ApiService {
    okHttpClientBuilder.addInterceptor(logInterceptor)
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClientBuilder.build())
        .build()
    return retrofit.create(ApiService::class.java)
}

private fun invokeWithAuth(
    context: Context,
    authenticator: TokenAuthenticator
): ApiService {
    okHttpClientBuilder.addInterceptor(
        object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Authorization", "Bearer ${context.getToken()}")
                return chain.proceed(newRequest.build())
            }
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