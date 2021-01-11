package com.dc.todomvvmretrofitcoroutine.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.dc.todomvvmretrofitcoroutine.data.model.LoginModel
import com.dc.todomvvmretrofitcoroutine.data.repository.LoginRepository
import com.dc.todomvvmretrofitcoroutine.utils.setToken
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

            val loginResponse: LoginModel = loginRepository.userLogin(email, password)
            if (loginResponse.status == 1) {
                loginResponse.accessToken?.let {
                    getApplication<Application>().setToken(it)
                }
                loginResponse.message?.let {
                    emit(State.Success(it))
                }
            } else {
                loginResponse.message?.let {
                    emit(State.Error(it))
                }
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val message: String) : State()
        data class ValidationError(val emailError: String?, val passwordError: String?) :
            State()

        data class Error(val message: String) : State()
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val loginRepository: LoginRepository, private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(loginRepository,application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}