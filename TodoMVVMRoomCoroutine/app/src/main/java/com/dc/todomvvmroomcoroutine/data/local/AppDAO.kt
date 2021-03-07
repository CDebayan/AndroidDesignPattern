package com.dc.todomvvmroomcoroutine.data.local

import androidx.room.*
import com.dc.todomvvmroomcoroutine.data.model.TodoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDAO {
    @Query("SELECT * FROM todo")
    fun todoList(): Flow<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todoModel: TodoModel): Long

    @Update
    suspend fun updateTodo(todoModel: TodoModel): Int

    @Delete
    fun deleteTodo(todoModel: TodoModel): Int
}