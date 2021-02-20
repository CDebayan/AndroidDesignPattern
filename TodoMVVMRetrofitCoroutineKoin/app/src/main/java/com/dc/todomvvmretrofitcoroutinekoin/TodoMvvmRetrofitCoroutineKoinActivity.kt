package com.dc.todomvvmretrofitcoroutinekoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.todomvvmretrofitcoroutinekoin.databinding.ActivityTodoMvvmRetrofitCoroutineKoinBinding

class TodoMvvmRetrofitCoroutineKoinActivity : AppCompatActivity() {
    private val binding: ActivityTodoMvvmRetrofitCoroutineKoinBinding by lazy {
        ActivityTodoMvvmRetrofitCoroutineKoinBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}