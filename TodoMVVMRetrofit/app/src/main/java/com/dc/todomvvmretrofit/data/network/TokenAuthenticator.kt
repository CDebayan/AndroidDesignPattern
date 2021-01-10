package com.dc.todomvvmretrofit.data.network

import android.content.Context
import com.dc.todomvvmretrofit.data.model.GeneralResponse
import com.dc.todomvvmretrofit.utils.getToken
import com.dc.todomvvmretrofit.utils.setToken
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TokenAuthenticator(private val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val newToken = getNewToken(context)
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private fun getNewToken(context: Context): String {
        val response : retrofit2.Response<GeneralResponse> =
            Retrofit.Builder().baseUrl(RetrofitClient.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java).refreshToken("Bearer ${context.getToken()}").execute()

        context.setToken(response.body()?.message)
        response.body()?.message?.let {
            return it
        }
        return ""
    }
}