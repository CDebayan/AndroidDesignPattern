package com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dc.todomvvmretrofitcoroutinekoin.base.GeneralState
import com.dc.todomvvmretrofitcoroutinekoin.base.GeneralResponse
import com.dc.todomvvmretrofitcoroutinekoin.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinekoin.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todoListState: MutableLiveData<GeneralState<List<TodoModel>>> = MutableLiveData()
    val todoListState get() = _todoListState
    val todoList get() = repository.todoList

    init {
        viewModelScope.launch(IO) {
            fetchTodoList()
        }
    }

    private suspend fun fetchTodoList() {
        _todoListState.postValue(GeneralState.Loading)
        val response = repository.todoList()
        if (response.status == 1) {
            _todoListState.postValue(GeneralState.Success(data = null))
        } else {
            _todoListState.postValue(
                GeneralState.Error(
                    status = response.status,
                    message = response.message
                )
            )
        }
    }

    fun deleteTodo(todoId: Int) = liveData<GeneralState<GeneralResponse>>(IO) {
        emit(GeneralState.Loading)
        val response = repository.deleteTodo(todoId)
        if (response.status == 1) {
            emit(GeneralState.Success(message = response.message, data = null))
        } else {
            emit(GeneralState.Error(response.status, response.message))
        }
    }

    fun fetchList() {
        viewModelScope.launch(IO) {
            fetchTodoList()
        }
    }
}
