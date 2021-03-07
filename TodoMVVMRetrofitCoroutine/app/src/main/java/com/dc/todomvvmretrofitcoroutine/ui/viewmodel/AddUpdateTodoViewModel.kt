package com.dc.todomvvmretrofitcoroutine.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.dc.todomvvmretrofitcoroutine.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutine.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers

class AddUpdateTodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private var titleError: String? = null
    private var descriptionError: String? = null
    private var dateTimeError: String? = null
    private var priorityError: String? = null

    fun addTodo(
        title: String,
        description: String,
        dateTime: String,
        priority: String
    ) = liveData(Dispatchers.IO) {
        val hasError = validateFields(title, description, dateTime, priority)
        if (hasError) {
            emit(
                State.ValidationError(
                    titleError,
                    descriptionError,
                    dateTimeError,
                    priorityError
                )
            )
        } else {
            emit(State.Loading)
            val response = repository.addTodo(
                TodoModel(
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    priority = priority
                )
            )
            if (response.status == 1) {
                emit(State.Success(message = response.message, data = response.data))
            } else {
                emit(State.Error(message = response.message))
            }
        }
    }

    fun updateTodo(
        todoId: Int,
        title: String,
        description: String,
        dateTime: String,
        priority: String
    ) = liveData(Dispatchers.IO) {
        val hasError = validateFields(title, description, dateTime, priority)

        if (hasError) {
            emit(
                State.ValidationError(
                    titleError,
                    descriptionError,
                    dateTimeError,
                    priorityError
                )
            )
        } else {
            emit(State.Loading)
            val todoModel = TodoModel(
                todoId = todoId,
                title = title,
                description = description,
                dateTime = dateTime,
                priority = priority
            )
            val response = repository.updateTodo(
                todoId = todoId,
                todoModel = todoModel
            )
            if (response.status == 1) {
                emit(State.Success(message = response.message, data = todoModel))
            } else {
                emit(State.Error(message = response.message))
            }
        }
    }

    private fun validateFields(
        title: String,
        description: String,
        dateTime: String,
        priority: String
    ): Boolean {
        var hasError = false
        if (title.isEmpty()) {
            hasError = true
            titleError = "Please enter title"
        }else{
            titleError = null
        }
        if (description.isEmpty()) {
            hasError = true
            descriptionError = "Please enter description"
        }else{
            descriptionError = null
        }
        if (dateTime.isEmpty()) {
            hasError = true
            dateTimeError = "Please enter date time"
        }else{
            dateTimeError = null
        }
        if (priority.isEmpty()) {
            hasError = true
            priorityError = "Please select priority"
        }else{
            priorityError = null
        }
        return hasError
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(private val repository: TodoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddUpdateTodoViewModel::class.java)) {
                return AddUpdateTodoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val message: String?, val data: TodoModel?) : State()
        data class ValidationError(
            val titleError: String?,
            val descriptionError: String?,
            val dateTimeError: String?,
            val priorityError: String?,
        ) : State()

        data class Error(val message: String?) : State()
    }
}