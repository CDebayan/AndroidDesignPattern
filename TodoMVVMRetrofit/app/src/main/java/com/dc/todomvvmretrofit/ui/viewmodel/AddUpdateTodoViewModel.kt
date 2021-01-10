package com.dc.todomvvmretrofit.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.dc.todomvvmretrofit.base.viewmodel.BaseViewModel
import com.dc.todomvvmretrofit.data.model.GeneralResponse
import com.dc.todomvvmretrofit.data.model.AddTodoModel
import com.dc.todomvvmretrofit.data.model.TodoModel
import com.dc.todomvvmretrofit.todo.repository.TodoRepository
import com.dc.todomvvmretrofit.utils.checkConnectivityError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUpdateTodoViewModel(application: Application) : BaseViewModel(application) {
    private var titleError: String? = null
    private var descriptionError: String? = null
    private var dateTimeError: String? = null
    private var priorityError: String? = null

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

     fun addTodo(
        title: String,
        description: String,
        dateTime: String,
        priority: String
    ): MutableLiveData<AddUpdateTodoState> {

        val addTodoResponse: MutableLiveData<AddUpdateTodoState> = MutableLiveData()

        val hasError = validateFields(title, description, dateTime, priority)

        if (hasError) {
            addTodoResponse.postValue(
                AddUpdateTodoState.ValidationError(
                    titleError,
                    descriptionError,
                    dateTimeError,
                    priorityError
                )
            )
        } else {
            addTodoResponse.postValue(AddUpdateTodoState.Loading)
            retrofitService.addTodo(
                TodoModel(
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    priority = priority
                )
            ).enqueue(object :
                Callback<AddTodoModel> {
                override fun onFailure(call: Call<AddTodoModel>, t: Throwable) {
                    addTodoResponse.postValue(AddUpdateTodoState.Error(checkConnectivityError(t).message))
                }

                override fun onResponse(
                    call: Call<AddTodoModel>,
                    response: Response<AddTodoModel>
                ) {
                    response.body()?.data?.let {
                        TodoRepository.addTodo(it)
                    }
                    response.body()?.message?.let {
                        addTodoResponse.postValue(AddUpdateTodoState.Success(it))
                    }
                }
            })
        }
        return addTodoResponse
    }

    fun updateTodo(
        todoId: Int,
        title: String,
        description: String,
        dateTime: String,
        priority: String
    ): MutableLiveData<AddUpdateTodoState> {
        val updateTodoResponse: MutableLiveData<AddUpdateTodoState> = MutableLiveData()

        val hasError = validateFields(title, description, dateTime, priority)

        if (hasError) {
            updateTodoResponse.postValue(
                AddUpdateTodoState.ValidationError(
                    titleError,
                    descriptionError,
                    dateTimeError,
                    priorityError
                )
            )
        } else {
            updateTodoResponse.postValue(AddUpdateTodoState.Loading)
            retrofitService.updateTodo(
                todoId = todoId,
                todoModel = TodoModel(
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    priority = priority
                )
            ).enqueue(object :
                Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    updateTodoResponse.postValue(AddUpdateTodoState.Error(checkConnectivityError(t).message))
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.body()?.status == 1) {
                        TodoRepository.updateTodo(
                            todoId = todoId,
                            todoModel = TodoModel(
                                todoId = todoId,
                                title = title,
                                description = description,
                                dateTime = dateTime,
                                priority = priority
                            )
                        )
                    }
                    response.body()?.message?.let {
                        updateTodoResponse.postValue(AddUpdateTodoState.Success(it))
                    }
                }
            })
        }
        return updateTodoResponse
    }
    sealed class AddUpdateTodoState {
        object Loading : AddUpdateTodoState()
        data class Success(val message: String) : AddUpdateTodoState()
        data class ValidationError(
            val titleError: String?,
            val descriptionError: String?,
            val dateTimeError: String?,
            val priorityError: String?,
        ) : AddUpdateTodoState()

        data class Error(val message: String) : AddUpdateTodoState()
    }
}

