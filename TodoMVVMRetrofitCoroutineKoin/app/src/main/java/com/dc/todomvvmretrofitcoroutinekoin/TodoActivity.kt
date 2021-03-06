package com.dc.todomvvmretrofitcoroutinekoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.todomvvmretrofitcoroutinekoin.databinding.ActivityTodoBinding

class TodoActivity : AppCompatActivity() {
    private val binding: ActivityTodoBinding by lazy {
        ActivityTodoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}