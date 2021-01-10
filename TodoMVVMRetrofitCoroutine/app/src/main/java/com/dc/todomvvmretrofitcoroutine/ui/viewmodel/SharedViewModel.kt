package com.dc.todomvvmretrofitcoroutine.ui.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dc.todomvvmretrofitcoroutine.data.model.TodoModel

class SharedViewModel : ViewModel() {
    private val _todoList: MutableLiveData<List<TodoModel>> = MutableLiveData()
    val todoList: LiveData<List<TodoModel>> get() = _todoList

    fun addTodo(todoModel: TodoModel?) {
        todoModel?.let {
            val list: MutableList<TodoModel> = todoList.value as MutableList<TodoModel>
            list.add(it)
            _todoList.postValue(list)
        }
    }

    fun addTodoList(data: List<TodoModel>?) {
        data?.let {
            _todoList.postValue(data)
        }
    }

    fun deleteTodo(todoId: Int) {
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

    fun updateTodo(todoModel: TodoModel) {
        val list: MutableList<TodoModel> = todoList.value as MutableList<TodoModel>
        val position: Int = list.indexOfFirst {
            it.todoId == todoModel.todoId
        }
        list[position] = todoModel
        _todoList.postValue(list)
    }
}