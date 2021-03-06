package com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dc.todomvvmretrofitcoroutinekoin.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinekoin.data.repository.TodoRepository
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
                emit(State.Success(message = response.message))
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
                emit(State.Success(message = response.message))
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
        }
        if (description.isEmpty()) {
            hasError = true
            descriptionError = "Please enter description"
        }
        if (dateTime.isEmpty()) {
            hasError = true
            dateTimeError = "Please enter date time"
        }
        if (priority.isEmpty()) {
            hasError = true
            priorityError = "Please select priority"
        }
        return hasError
    }


    sealed class State {
        object Loading : State()
        data class Success(val message: String?) : State()
        data class ValidationError(
            val titleError: String?,
            val descriptionError: String?,
            val dateTimeError: String?,
            val priorityError: String?,
        ) : State()
        data class Error(val message: String?) : State()
    }
}