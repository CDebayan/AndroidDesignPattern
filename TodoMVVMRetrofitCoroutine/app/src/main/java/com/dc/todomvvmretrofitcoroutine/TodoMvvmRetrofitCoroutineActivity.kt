package com.dc.todomvvmretrofitcoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.todomvvmretrofitcoroutine.databinding.ActivityTodoMvvmRetrofitCoroutineBinding

class TodoMvvmRetrofitCoroutineActivity : AppCompatActivity() {
    private val binding : ActivityTodoMvvmRetrofitCoroutineBinding by lazy {
        ActivityTodoMvvmRetrofitCoroutineBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}