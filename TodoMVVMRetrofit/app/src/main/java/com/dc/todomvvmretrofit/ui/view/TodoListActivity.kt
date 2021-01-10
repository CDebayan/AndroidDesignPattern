package com.dc.todomvvmretrofit.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.todomvvmretrofit.databinding.ActivityTodoListBinding
import com.dc.todomvvmretrofit.ui.adapter.TodoListAdapter
import com.dc.todomvvmretrofit.data.model.TodoModel
import com.dc.todomvvmretrofit.ui.viewmodel.TodoListViewModel
import com.dc.todomvvmretrofit.utils.*

class TodoListActivity : BaseActivity() {
    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var binding: ActivityTodoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setBaseView(binding.root, title = "My Todos")

        todoListViewModel = ViewModelProvider(this@TodoListActivity).get(TodoListViewModel::class.java)

        onClickListener()
        observers()

        fetchList()
    }

    private fun onClickListener() {
        binding.retryButton.setOnClickListener {
            fetchList()
        }
        binding.addButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "add")
            openActivity(AddUpdateActivity::class.java, bundleKey = "bundleData", bundle = bundle)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observers() {
        todoListViewModel.todoList.observe(this, {
            if (it != null && it.isNotEmpty()) {
                showHideViews(showRecycler = true)
                setRecyclerView(it)
            } else {
                binding.placeholder.text = "No Data"
                showHideViews(showPlaceHolder = true)
            }
        })
    }

    private fun fetchList() {
        todoListViewModel.fetchTodoList().observe(this, { state ->
            when (state) {
                is TodoListViewModel.TodoListState.Loading -> showHideViews(showLoader = true)
                is TodoListViewModel.TodoListState.Error -> {
                    showHideViews(showRetry = true, showPlaceHolder = true)
                    binding.placeholder.text = state.message
                }
            }
        })
    }

    private fun setRecyclerView(todoList: List<TodoModel>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@TodoListActivity)
        binding.recyclerView.adapter = TodoListAdapter(todoList, object : ItemClickListener {
            override fun onItemClick(position: Int, option: String) {
                if (option == "delete") {
                    deleteTodo(todoList[position].todoId)

                } else if (option == "update") {
                    updateTodo(todoList[position])
                }
            }
        })
    }

    private fun updateTodo(todoModel: TodoModel) {
        val bundle = Bundle()
        bundle.putString("type", "update")
        bundle.putParcelable("todoData", todoModel)
        openActivity(AddUpdateActivity::class.java, bundleKey = "bundleData", bundle = bundle)
    }

    private fun deleteTodo(todoId: Int?) {
        todoId?.let {
            todoListViewModel.deleteTodo(it).observe(this, { state ->
                when (state) {
                    is TodoListViewModel.TodoListState.Loading -> showHideViews(showLoader = true,showRecycler = true)
                    is TodoListViewModel.TodoListState.Error -> {
                        showHideViews(showLoader = false,showRecycler = true)
                        showToast(state.message)
                    }
                    is TodoListViewModel.TodoListState.Success -> {
                        showToast(state.message)
                    }
                }
            })
        }
    }

    private fun showHideViews(
        showRecycler: Boolean = false,
        showPlaceHolder: Boolean = false,
        showRetry: Boolean = false,
        showLoader: Boolean = false
    ) {
        if (showRecycler) {
            binding.recyclerView.show()
        } else {
            binding.recyclerView.gone()
        }

        if (showPlaceHolder) {
            binding.placeholder.show()
        } else {
            binding.placeholder.gone()
        }

        if (showRetry) {
            binding.retryButton.show()
        } else {
            binding.retryButton.gone()
        }

        if (showLoader) {
            binding.progress.show()
        } else {
            binding.progress.gone()
        }
    }
}