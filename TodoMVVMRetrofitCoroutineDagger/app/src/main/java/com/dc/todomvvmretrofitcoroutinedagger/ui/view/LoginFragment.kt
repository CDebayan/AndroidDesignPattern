package com.dc.todomvvmretrofitcoroutinedagger.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofitcoroutinedagger.R
import com.dc.todomvvmretrofitcoroutinedagger.base.ViewModelProviderFactory
import com.dc.todomvvmretrofitcoroutinedagger.databinding.FragmentLoginBinding
import com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel.LoginViewModel
import com.dc.todomvvmretrofitcoroutinedagger.utils.*
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    @Inject lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)
        setOnClickListener()
    }

    private fun setOnClickListener() {
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
            viewModel.login(email, password)
                .observe(viewLifecycleOwner, Observer(this::handleState))
        }
    }

    private fun handleState(state: LoginViewModel.State) {
        when (state) {
            is LoginViewModel.State.Loading -> setLoading(true)
            is LoginViewModel.State.Success -> {
                setLoading(false)
                showToast(state.message)
                requireActivity().nextFragment(R.id.action_loginFragment_to_todoListFragment)
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