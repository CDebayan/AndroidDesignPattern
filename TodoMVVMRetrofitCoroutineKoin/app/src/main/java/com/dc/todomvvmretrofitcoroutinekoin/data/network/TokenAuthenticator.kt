package com.dc.todomvvmretrofitcoroutinekoin.data.network

import android.content.Context
import com.dc.todomvvmretrofitcoroutinekoin.data.model.GeneralResponse
import com.dc.todomvvmretrofitcoroutinekoin.utils.getToken
import com.dc.todomvvmretrofitcoroutinekoin.utils.setToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(private val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val newToken = runBlocking {
            getNewToken(context)
        }
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private suspend fun getNewToken(context: Context): String {
        val response: GeneralResponse =
            Retrofit.Builder().baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java).refreshToken("Bearer ${context.getToken()}")
        context.setToken(response.message)
        response.message?.let {
            return it
        }
        return ""
    }
}