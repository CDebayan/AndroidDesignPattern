package com.dc.todomvvmretrofit.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dc.todomvvmretrofit.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        baseClickListener()
    }

    private fun baseClickListener() {
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    protected fun setBaseView(layout: View, title: String = "",showBack : Boolean = false) {
        binding.baseLayout.addView(layout)
        binding.baseTitle.text = title

        if(showBack){
            binding.backButton.visibility = View.VISIBLE
        }else{
            binding.backButton.visibility = View.GONE
        }
    }
}