package com.dc.todomvvmretrofit.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofit.ui.viewmodel.LoginViewModel
import com.dc.todomvvmretrofit.data.network.RetrofitClient
import com.dc.todomvvmretrofit.databinding.ActivityLoginBinding
import com.dc.todomvvmretrofit.utils.*

class LoginActivity : AppCompatActivity() {

    private  val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginViewModel.ViewModelFactory(RetrofitClient.invokeWithOutAuth(),application)
        ).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onClickListener()
    }

    private fun onClickListener() {
        binding.email.addTextChangedListener {
            if (binding.emailLayout.error != null) {
                binding.emailLayout.error = null
            }
        }
        binding.password.addTextChangedListener {
            if (binding.passwordLayout.error != null) {
                binding.passwordLayout.error = null
            }
        }


        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            viewModel.login(email, password).observe(this, Observer(this::handleState))
        }
    }

    private fun handleState(state: LoginViewModel.State) {
        when (state) {
            is LoginViewModel.State.Loading -> setLoading(true)
            is LoginViewModel.State.Success -> {
                setLoading(false)
                showToast(state.message)
                openActivity(TodoListActivity::class.java, clearTask = true)
            }
            is LoginViewModel.State.Error -> {
                setLoading(false)
                showToast(state.message)
            }
            is LoginViewModel.State.ValidationError -> {
                binding.emailLayout.error = state.emailError
                binding.passwordLayout.error = state.passwordError
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.emailLayout.disable()
            binding.passwordLayout.disable()
            binding.loginButton.invisible()
            binding.progressBar.show()
            binding.loginButton.disable()

        } else {
            binding.emailLayout.enable()
            binding.passwordLayout.enable()
            binding.loginButton.show()
            binding.progressBar.gone()
            binding.loginButton.enable()
        }
    }
}