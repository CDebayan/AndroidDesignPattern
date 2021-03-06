package com.dc.todomvvmretrofitcoroutine.data.repository

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.dc.todomvvmretrofitcoroutine.data.model.AddTodoModel
import com.dc.todomvvmretrofitcoroutine.data.model.GeneralResponse
import com.dc.todomvvmretrofitcoroutine.data.model.TodoListModel
import com.dc.todomvvmretrofitcoroutine.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutine.data.network.ApiService
import com.dc.todomvvmretrofitcoroutine.utils.checkConnectivityError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TodoRepository() {
    private lateinit var apiService: ApiService
    private val _todoList: MutableLiveData<List<TodoModel>> = MutableLiveData()
    val todoList get() = _todoList

    private constructor(apiService: ApiService) : this() {
        this.apiService = apiService
    }

    companion object {
        private lateinit var todoRepository: TodoRepository

        fun instance(apiService: ApiService): TodoRepository {
            if (!::todoRepository.isInitialized) {
                todoRepository = TodoRepository(apiService)
            }
            return todoRepository
        }
    }

    suspend fun todoList(): TodoListModel {
        return withContext(IO) {
            try {
                val response = apiService.todoList()
                if (response.status == 1) {
                    response.data?.let {
                        _todoList.postValue(it)
                    }
                }
                response
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                TodoListModel(status = error.status, message = error.message)
            }
        }
    }

    suspend fun addTodo(todoModel: TodoModel): AddTodoModel {
        return withContext(IO) {
            try {
                val response = apiService.addTodo(todoModel)
                if (response.status == 1) {
                    response.data?.let {
                        val list: MutableList<TodoModel> = todoList.value as MutableList<TodoModel>
                        list.add(it)
                        _todoList.postValue(list)
                    }
                }
                response
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                AddTodoModel(status = error.status, message = error.message)
            }
        }
    }

    suspend fun updateTodo(todoId: Int, todoModel: TodoModel): GeneralResponse {
        return withContext(IO) {
            try {
                val response = apiService.updateTodo(todoId, todoModel)
                if (response.status == 1) {
                    val list: MutableList<TodoModel> = todoList.value as MutableList<TodoModel>
                    val position: Int = list.indexOfFirst {
                        it.todoId == todoModel.todoId
                    }
                    list[position] = todoModel
                    _todoList.postValue(list)
                }
                response
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                GeneralResponse(status = error.status, message = error.message)
            }
        }
    }

    suspend fun deleteTodo(todoId: Int): GeneralResponse {
        return withContext(IO) {
            try {
                val response = apiService.deleteTodo(todoId)
                if (response.status == 1) {
                    val list: MutableList<TodoModel> = todoList.value as MutableList<TodoModel>
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        list.removeIf {
                            it.todoId == todoId
                        }
                    } else {
                        list.remove(list.find {
                            it.todoId == todoId
                        })
                    }
                    _todoList.postValue(list)
                }
                response
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                GeneralResponse(status = error.status, message = error.message)
            }
        }
    }
}