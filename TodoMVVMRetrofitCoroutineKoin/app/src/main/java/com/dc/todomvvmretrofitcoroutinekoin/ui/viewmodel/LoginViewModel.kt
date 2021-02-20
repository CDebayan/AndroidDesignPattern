package com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.dc.todomvvmretrofitcoroutinekoin.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutinekoin.data.repository.LoginRepository
import com.dc.todomvvmretrofitcoroutinekoin.utils.setToken
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val loginRepository: LoginRepository, application: Application) : AndroidViewModel(application) {

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
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
            emit(State.ValidationError(emailError, passwordError))
        } else {
            emit(State.Loading)
            val response: LoginModel = loginRepository.userLogin(email, password)
            if (response.status == 1) {
                response.accessToken?.let {
                    getApplication<Application>().setToken(it)
                }
                emit(State.Success(response.message))
            } else {
                emit(State.Error(response.message))
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val message: String?) : State()
        data class ValidationError(val emailError: String?, val passwordError: String?) : State()
        data class Error(val message: String?) : State()
    }

}