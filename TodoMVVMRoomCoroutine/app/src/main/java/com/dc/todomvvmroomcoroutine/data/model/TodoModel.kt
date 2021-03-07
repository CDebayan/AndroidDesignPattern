package com.dc.todomvvmroomcoroutine.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Todo")
@Parcelize
data class TodoModel(
        @PrimaryKey(autoGenerate = true)
        val todoId: Int = 0,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "dateTime")
        val dateTime: String,
        @ColumnInfo(name = "priority")
        val priority: String
) : Parcelable
