package com.dc.todomvvmretrofitcoroutinekoin.data.network

import android.content.Context
import com.dc.todomvvmretrofitcoroutinekoin.base.GeneralResponse
import com.dc.todomvvmretrofitcoroutinekoin.utils.getToken
import com.dc.todomvvmretrofitcoroutinekoin.utils.setToken
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(private val context: Context,private val apiService: ApiService) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val newToken = runBlocking {
            getNewToken(context)
        }
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private suspend fun getNewToken(context: Context): String {
        val response: GeneralResponse = apiService.refreshToken("Bearer ${context.getToken()}")
        context.setToken(response.message)
        response.message?.let {
            return it
        }
        return ""
    }
}