package com.dc.todomvvmroomcoroutine.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dc.todomvvmroomcoroutine.data.local.AppDAO
import com.dc.todomvvmroomcoroutine.data.model.TodoModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TodoRepository() {
    private lateinit var appDAO: AppDAO

    private constructor(appDAO: AppDAO) : this() {
        this.appDAO = appDAO
    }

    companion object {
        private lateinit var todoRepository: TodoRepository

        fun instance(appDAO: AppDAO): TodoRepository {
            if (!::todoRepository.isInitialized) {
                todoRepository = TodoRepository(appDAO)
            }
            return todoRepository
        }
    }

    fun todoList(): LiveData<List<TodoModel>> {
        return appDAO.todoList().asLiveData()
    }

    suspend fun addTodo(todoModel: TodoModel): Long {
        return withContext(IO) {
            appDAO.addTodo(todoModel)
        }
    }

    suspend fun updateTodo(todoModel: TodoModel): Int {
        return withContext(IO) {
            appDAO.updateTodo(todoModel)
        }
    }

    suspend fun deleteTodo(todoModel: TodoModel): Int {
        return withContext(IO) {
            appDAO.deleteTodo(todoModel)
        }
    }
}