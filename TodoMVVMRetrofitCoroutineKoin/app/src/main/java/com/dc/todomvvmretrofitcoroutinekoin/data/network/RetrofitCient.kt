package com.dc.todomvvmretrofitcoroutinekoin.data.network

import android.content.Context
import com.dc.todomvvmretrofitcoroutinekoin.BuildConfig
import com.dc.todomvvmretrofitcoroutinekoin.utils.getToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitClient {
    const val BASE_URL: String = "http://192.168.0.7:4001/"
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private val logInterceptor = HttpLoggingInterceptor()

    fun invokeWithOutAuth(
        enableInterceptor: Boolean = true,
    ): ApiService {
        if (enableInterceptor && BuildConfig.DEBUG) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        okHttpClientBuilder.addInterceptor(logInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun invokeWithAuth(
        context: Context,
        enableInterceptor: Boolean = true,
    ): ApiService {
        if (enableInterceptor && BuildConfig.DEBUG) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        okHttpClientBuilder.addInterceptor(
            object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val newRequest = chain.request().newBuilder()
                    newRequest.addHeader("Authorization", "Bearer ${context.getToken()}")
                    return chain.proceed(newRequest.build())
                }
            })
            .authenticator(TokenAuthenticator(context))
            .addInterceptor(logInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}