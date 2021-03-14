package com.dc.todomvvmretrofitcoroutinedagger.data.repository

import com.dc.todomvvmretrofitcoroutinedagger.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutinedagger.data.network.ApiService
import com.dc.todomvvmretrofitcoroutinedagger.utils.RetrofitInstances
import com.dc.todomvvmretrofitcoroutinedagger.utils.checkConnectivityError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LoginRepository @Inject constructor(@Named(RetrofitInstances.WithOutAuth) private val apiService: ApiService) {

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