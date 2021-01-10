package com.dc.todomvvmretrofitcoroutine.data.repository

import com.dc.todomvvmretrofitcoroutine.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutine.data.network.ApiService
import com.dc.todomvvmretrofitcoroutine.utils.checkConnectivityError
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