package com.dc.todomvvmretrofit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofit.data.model.LoginModel
import com.dc.todomvvmretrofit.data.network.ApiService
import com.dc.todomvvmretrofit.utils.checkConnectivityError
import com.dc.todomvvmretrofit.utils.setToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val apiService: ApiService,application: Application) : AndroidViewModel(application) {

    fun login(email: String, password: String): MutableLiveData<State> {
        val loginResponse: MutableLiveData<State> = MutableLiveData()

        var hasError = false
        var emailError: String? = null
        var passwordError: String? = null

        if (email.isEmpty()) {
            hasError = true
            emailError = "Please enter email"
        }
        if (password.isEmpty()) {
            hasError = true
            passwordError = "Please enter password"
        }

        if (hasError) {
            loginResponse.postValue(State.ValidationError(emailError, passwordError))
        } else {
            loginResponse.postValue(State.Loading)
            apiService.userLogin(email, password).enqueue(object : Callback<LoginModel> {
                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    loginResponse.postValue(State.Error(checkConnectivityError(t).message))
                }
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    if (response.body()?.status == 1) {
                        response.body()?.accessToken?.let {
                            getApplication<Application>().setToken(it)
                        }
                        response.body()?.message?.let {
                            loginResponse.postValue(State.Success(it))
                        }
                    } else {
                        response.body()?.message?.let {
                            loginResponse.postValue(State.Error(it))
                        }
                    }
                }
            })
        }
        return loginResponse
    }
    sealed class State {
        object Loading : State()
        data class Success(val message: String) : State()
        data class ValidationError(val emailError: String?, val passwordError: String?) : State()
        data class Error(val message: String) : State()
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val apiService: ApiService, private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(apiService,application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}





