package com.dc.todomvvmroomcoroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmroomcoroutine.data.local.AppDatabase
import com.dc.todomvvmroomcoroutine.data.repository.TodoRepository
import com.dc.todomvvmroomcoroutine.databinding.ActivityTodoBinding
import com.dc.todomvvmroomcoroutine.ui.adapter.TodoListAdapter
import com.dc.todomvvmroomcoroutine.ui.viewmodel.TodoListViewModel

class TodoActivity : AppCompatActivity() {
    private val binding: ActivityTodoBinding by lazy {
        ActivityTodoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}