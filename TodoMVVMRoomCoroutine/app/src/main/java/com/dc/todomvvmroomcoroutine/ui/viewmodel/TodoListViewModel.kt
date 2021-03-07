package com.dc.todomvvmroomcoroutine.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.dc.todomvvmroomcoroutine.data.model.TodoModel
import com.dc.todomvvmroomcoroutine.data.repository.TodoRepository
import com.dc.todomvvmroomcoroutine.utils.GeneralState
import kotlinx.coroutines.Dispatchers.IO


class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val todoList get() = repository.todoList()

    fun deleteTodo(todoModel: TodoModel) = liveData(IO) {
        emit(GeneralState.Loading)
        val response = repository.deleteTodo(todoModel)
        if (response > 0) {
            emit(GeneralState.Success(message = "Delete Successful"))
        } else {
            emit(GeneralState.Error(message = "Delete Failed"))
        }
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: TodoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
                return TodoListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
