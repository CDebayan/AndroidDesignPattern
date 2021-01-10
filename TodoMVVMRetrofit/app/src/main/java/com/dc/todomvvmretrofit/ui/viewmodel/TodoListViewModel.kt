package com.dc.todomvvmretrofit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dc.todomvvmretrofit.data.model.GeneralResponse
import com.dc.todomvvmretrofit.base.viewmodel.BaseViewModel
import com.dc.todomvvmretrofit.data.model.TodoListModel
import com.dc.todomvvmretrofit.data.model.TodoModel
import com.dc.todomvvmretrofit.todo.repository.TodoRepository
import com.dc.todomvvmretrofit.utils.checkConnectivityError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoListViewModel(application: Application) : BaseViewModel(application) {
    val todoList: LiveData<List<TodoModel>> get() = TodoRepository.todoList

    fun fetchTodoList(): MutableLiveData<TodoListState> {
        val todoListResponse: MutableLiveData<TodoListState> = MutableLiveData()
        todoListResponse.postValue(TodoListState.Loading)
        retrofitService.todoList()
            .enqueue(object : Callback<TodoListModel> {
                override fun onFailure(call: Call<TodoListModel>, t: Throwable) {
                    val error = checkConnectivityError(t)
                    todoListResponse.postValue(TodoListState.Error(error.status, error.message))
                }

                override fun onResponse(
                    call: Call<TodoListModel>,
                    response: Response<TodoListModel>
                ) {
                    TodoRepository.addTodoList(response.body()?.data)
                    todoListResponse.postValue(TodoListState.Success(""))
                }
            })
        return todoListResponse

    }

    fun deleteTodo(todoId: Int): MutableLiveData<TodoListState> {
        val deleteTodoResponse: MutableLiveData<TodoListState> = MutableLiveData()
        deleteTodoResponse.postValue(TodoListState.Loading)
        retrofitService.deleteTodo(todoId).enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    val error = checkConnectivityError(t)
                    deleteTodoResponse.postValue(TodoListState.Error(error.status, error.message))
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.body()?.status == 1) {
                        TodoRepository.deleteTodo(todoId)
                    }
                    deleteTodoResponse.postValue(TodoListState.Success(response.body()?.message))
                }
            })

        return deleteTodoResponse
    }
    sealed class TodoListState {
        object Loading : TodoListState()
        data class Success(val message: String?) : TodoListState()
        data class Error(val status: Int, val message: String) : TodoListState()
    }
}

