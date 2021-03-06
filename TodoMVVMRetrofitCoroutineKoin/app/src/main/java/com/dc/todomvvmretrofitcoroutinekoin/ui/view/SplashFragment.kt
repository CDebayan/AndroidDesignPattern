package com.dc.todomvvmretrofitcoroutinekoin.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.dc.todomvvmretrofitcoroutinekoin.R
import com.dc.todomvvmretrofitcoroutinekoin.databinding.FragmentSplashBinding
import com.dc.todomvvmretrofitcoroutinekoin.utils.getToken
import com.dc.todomvvmretrofitcoroutinekoin.utils.nextFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            if (requireContext().getToken() != null) {
                requireActivity().nextFragment(R.id.action_splashFragment_to_todoListFragment)
            } else {
                requireActivity().nextFragment(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }

}