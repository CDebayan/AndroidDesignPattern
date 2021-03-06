package com.dc.todomvvmretrofitcoroutinekoin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.todomvvmretrofitcoroutinekoin.R
import com.dc.todomvvmretrofitcoroutinekoin.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinekoin.databinding.ChildTodoListBinding
import com.dc.todomvvmretrofitcoroutinekoin.utils.RecyclerViewOption
import com.dc.todomvvmretrofitcoroutinekoin.utils.convertDateTime

class TodoListAdapter : ListAdapter<TodoModel, TodoListAdapter.ViewHolder>(DiffCallBack) {
    private var onItemClickListener: ((data: TodoModel, option: RecyclerViewOption) -> Unit)? = null

    fun setOnItemClickListener(listener: (data: TodoModel, option: RecyclerViewOption) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChildTodoListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToViews(getItem(position))
        holder.setOnClickListener(getItem(position))
    }

    override fun submitList(list: List<TodoModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class ViewHolder(private val binding: ChildTodoListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setOnClickListener(todoModel: TodoModel) {
            binding.deleteButton.setOnClickListener {
                onItemClickListener?.let {
                    it(todoModel, RecyclerViewOption.Delete)
                }
            }
            binding.editButton.setOnClickListener {
                onItemClickListener?.let {
                    it(todoModel, RecyclerViewOption.Edit)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun setDataToViews(todoModel: TodoModel) {
            binding.title.text = todoModel.title
            binding.description.text = todoModel.description
            binding.dateTime.text = convertDateTime(todoModel.dateTime)
            binding.priority.text = todoModel.priority
            when (todoModel.priority) {
                "Low" -> {
                    binding.priorityIcon.setImageResource(R.drawable.ic_circle_green)
                }
                "Medium" -> {
                    binding.priorityIcon.setImageResource(R.drawable.ic_circle_yellow)
                }
                "High" -> {
                    binding.priorityIcon.setImageResource(R.drawable.ic_circle_red)
                }
            }
        }
    }

    private object DiffCallBack : DiffUtil.ItemCallback<TodoModel>() {
        override fun areItemsTheSame(oldItem: TodoModel, newItem: TodoModel): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: TodoModel, newItem: TodoModel): Boolean {
            return oldItem.todoId == newItem.todoId
        }
    }
}
