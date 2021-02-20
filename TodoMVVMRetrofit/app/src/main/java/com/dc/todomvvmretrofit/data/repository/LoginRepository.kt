package com.dc.todomvvmretrofit.data.repository

import androidx.lifecycle.MutableLiveData
import com.dc.todomvvmretrofit.data.model.LoginModel
import com.dc.todomvvmretrofit.data.network.ApiService
import com.dc.todomvvmretrofit.ui.viewmodel.LoginViewModel
import com.dc.todomvvmretrofit.utils.checkConnectivityError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun userLogin(email: String, password: String): MutableLiveData<LoginViewModel.State> {
        val loginResponse: MutableLiveData<LoginViewModel.State> = MutableLiveData()
        loginResponse.postValue(LoginViewModel.State.Loading)
        apiService.userLogin(email, password).enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.body()?.status == 1) {
                    loginResponse.postValue(LoginViewModel.State.Success(response.body() as LoginModel))
                } else {
                    loginResponse.postValue(LoginViewModel.State.Error(response.body()?.message))
                }
            }
            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                loginResponse.postValue(LoginViewModel.State.Error(checkConnectivityError(t).message))
            }
        })
        return loginResponse
    }
}