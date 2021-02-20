package com.dc.todomvvmretrofitcoroutinekoin.data.repository

import com.dc.todomvvmretrofitcoroutinekoin.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutinekoin.data.network.ApiService
import com.dc.todomvvmretrofitcoroutinekoin.utils.checkConnectivityError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class LoginRepository(private val apiService: ApiService) {
    suspend fun userLogin(email: String, password: String): LoginModel {
        return withContext(IO) {
            try {
                apiService.userLogin(email, password)
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                LoginModel(status = error.status, message = error.message)
            }
        }
    }
}