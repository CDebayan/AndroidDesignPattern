package com.dc.todomvvmretrofitcoroutine.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.todomvvmretrofitcoroutine.R
import com.dc.todomvvmretrofitcoroutine.base.BaseFragment
import com.dc.todomvvmretrofitcoroutine.base.ChildView
import com.dc.todomvvmretrofitcoroutine.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutine.data.network.RetrofitClient
import com.dc.todomvvmretrofitcoroutine.data.repository.TodoRepository
import com.dc.todomvvmretrofitcoroutine.databinding.FragmentTodoListBinding
import com.dc.todomvvmretrofitcoroutine.ui.adapter.TodoListAdapter
import com.dc.todomvvmretrofitcoroutine.ui.viewmodel.TodoListViewModel
import com.dc.todomvvmretrofitcoroutine.utils.*

class TodoListFragment : BaseFragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val adapter = TodoListAdapter()
    private val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(
            this,
            TodoListViewModel.ViewModelFactory(
                TodoRepository.instance(
                    RetrofitClient.invokeWithAuth(
                        requireContext()
                    )
                )
            )
        ).get(TodoListViewModel::class.java)
    }

    override fun getChildView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ChildView {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return ChildView(
            view = binding.root,
            title = "Todo",
            showBack = false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListener()
        observers()
        setRecyclerView()
    }

    private fun onClickListener() {
        binding.retryButton.setOnClickListener {
            viewModel.fetchList()
        }
        binding.addButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "add")
            requireActivity().nextFragment(
                R.id.action_todoListFragment_to_addTodoFragment,
                bundle = bundle
            )
        }

    }

    private fun observers() {
        viewModel.todoList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.todoListState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is GeneralState.Loading -> showHideViews(showLoader = true)
                is GeneralState.Error -> {
                    showHideViews(showRetry = true, showPlaceHolder = true)
                    binding.placeholder.text = state.message
                }
                is GeneralState.Success -> {
                    showHideViews(showRecycler = true)
                }
            }
        })
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { data, option ->
            when (option) {
                RecyclerViewOption.Delete -> {
                    deleteTodo(data.todoId)
                }
                RecyclerViewOption.Edit -> {
                    updateTodo(data)
                }
                else -> {
                }
            }
        }
    }

    private fun updateTodo(todoModel: TodoModel) {
        val bundle = Bundle()
        bundle.putString("type", "update")
        bundle.putParcelable("todoData", todoModel)
        requireActivity().nextFragment(
            R.id.action_todoListFragment_to_addTodoFragment,
            bundle = bundle
        )
    }

    private fun deleteTodo(todoId: Int?) {
        todoId?.let {
            viewModel.deleteTodo(it).observe(viewLifecycleOwner, { state ->
                when (state) {
                    is GeneralState.Loading -> showHideViews(showLoader = true, showRecycler = true)
                    is GeneralState.Error -> {
                        showHideViews(showLoader = false, showRecycler = true)
                        showToast(state.message)
                    }
                    is GeneralState.Success -> {
                        showHideViews(showLoader = false, showRecycler = true)
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