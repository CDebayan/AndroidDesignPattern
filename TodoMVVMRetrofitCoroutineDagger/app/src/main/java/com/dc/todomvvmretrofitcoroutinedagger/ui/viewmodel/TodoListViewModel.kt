package com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel

import androidx.lifecycle.*
import com.dc.todomvvmretrofitcoroutinedagger.base.GeneralState
import com.dc.todomvvmretrofitcoroutinedagger.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinedagger.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

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

    fun deleteTodo(todoId: Int) = liveData(IO) {
        emit(GeneralState.Loading)
        val response = repository.deleteTodo(todoId)
        if (response.status == 1) {
            emit(GeneralState.Success(message = response.message,data = null))
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
