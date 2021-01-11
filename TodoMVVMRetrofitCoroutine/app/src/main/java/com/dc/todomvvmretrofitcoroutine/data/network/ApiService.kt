package com.dc.todomvvmretrofitcoroutine.data.network

import com.dc.todomvvmretrofitcoroutine.data.model.*
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginModel

    @GET("auth/refreshToken")
    suspend fun refreshToken(@Header("Authorization") token: String): GeneralResponse

    @GET("todo/todoList")
    suspend fun todoList(): TodoListModel

    @POST("todo/addTodo")
    suspend fun addTodo(@Body todoModel: TodoModel): AddTodoModel

    @PUT("todo/updateTodo/{id}")
    suspend fun updateTodo(@Path("id") todoId: Int, @Body todoModel: TodoModel): GeneralResponse

    @DELETE("todo/deleteTodo/{id}")
    suspend fun deleteTodo(@Path("id") todoId: Int): GeneralResponse
}