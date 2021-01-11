package com.dc.todomvvmretrofitcoroutine.data.repository

import com.dc.todomvvmretrofitcoroutine.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutine.data.network.ApiService
import com.dc.todomvvmretrofitcoroutine.utils.checkConnectivityError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class LoginRepository() {
    private lateinit var apiService: ApiService

    private constructor(apiService: ApiService) : this() {
        this.apiService = apiService
    }

    companion object {
        private lateinit var loginRepository: LoginRepository

        fun instance(apiService: ApiService): LoginRepository {
            if (!::loginRepository.isInitialized) {
                loginRepository = LoginRepository(apiService)
            }
            return loginRepository
        }
    }

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