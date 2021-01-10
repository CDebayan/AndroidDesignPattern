package com.dc.todomvvmretrofitcoroutine.data.repository

import com.dc.todomvvmretrofitcoroutine.data.model.AddTodoModel
import com.dc.todomvvmretrofitcoroutine.data.model.GeneralResponse
import com.dc.todomvvmretrofitcoroutine.data.model.TodoListModel
import com.dc.todomvvmretrofitcoroutine.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutine.data.network.ApiService
import com.dc.todomvvmretrofitcoroutine.utils.checkConnectivityError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TodoRepository(private val apiService: ApiService) {

    suspend fun todoList(): TodoListModel {
        return withContext(IO) {
            try {
                apiService.todoList()
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                TodoListModel(status = error.status, message = error.message)
            }
        }
    }

    suspend fun addTodo(todoModel: TodoModel): AddTodoModel {
        return withContext(IO) {
            try {
                apiService.addTodo(todoModel)
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                AddTodoModel(status = error.status, message = error.message)
            }
        }
    }

    suspend fun updateTodo(todoId : Int,todoModel: TodoModel): GeneralResponse {
        return withContext(IO) {
            try {
                apiService.updateTodo(todoId,todoModel)
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                GeneralResponse(status = error.status, message = error.message)
            }
        }
    }

    suspend fun deleteTodo(todoId: Int): GeneralResponse {
        return withContext(IO) {
            try {
                apiService.deleteTodo(todoId)
            } catch (e: Exception) {
                val error = checkConnectivityError(e)
                GeneralResponse(status = error.status, message = error.message)
            }
        }
    }
}