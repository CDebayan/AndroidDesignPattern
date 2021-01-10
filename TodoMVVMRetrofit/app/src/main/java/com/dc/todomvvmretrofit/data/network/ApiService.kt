package com.dc.todomvvmretrofit.data.network

import com.dc.todomvvmretrofit.data.model.GeneralResponse
import com.dc.todomvvmretrofit.data.model.LoginModel
import com.dc.todomvvmretrofit.data.model.AddTodoModel
import com.dc.todomvvmretrofit.data.model.TodoListModel
import com.dc.todomvvmretrofit.data.model.TodoModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @GET("auth/refreshToken")
    fun refreshToken(@Header("Authorization") token: String): Call<GeneralResponse>

    @GET("todo/todoList")
    fun todoList(): Call<TodoListModel>

    @POST("todo/addTodo")
    fun addTodo(@Body todoModel: TodoModel): Call<AddTodoModel>

    @PUT("todo/updateTodo/{id}")
    fun updateTodo(@Path("id") todoId: Int,@Body todoModel: TodoModel): Call<GeneralResponse>

    @DELETE("todo/deleteTodo/{id}")
    fun deleteTodo(@Path("id") todoId: Int): Call<GeneralResponse>
}