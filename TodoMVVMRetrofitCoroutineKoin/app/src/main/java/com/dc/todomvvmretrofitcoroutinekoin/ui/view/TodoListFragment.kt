package com.dc.todomvvmretrofitcoroutinekoin.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.todomvvmretrofitcoroutinekoin.R
import com.dc.todomvvmretrofitcoroutinekoin.base.BaseFragment
import com.dc.todomvvmretrofitcoroutinekoin.base.GeneralState
import com.dc.todomvvmretrofitcoroutinekoin.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinekoin.databinding.FragmentTodoListBinding
import com.dc.todomvvmretrofitcoroutinekoin.ui.adapter.TodoListAdapter
import com.dc.todomvvmretrofitcoroutinekoin.ui.viewmodel.TodoListViewModel
import com.dc.todomvvmretrofitcoroutinekoin.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class TodoListFragment : BaseFragment() {

    private lateinit var binding: FragmentTodoListBinding

    private val viewModel by viewModel<TodoListViewModel>()

    override fun getChildView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ChildViewModel {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return ChildViewModel(
            view = binding.root,
            title = "Todo",
            showBack = false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickListener()
        observers()
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
            setRecyclerView(it)
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

    private fun setRecyclerView(todoList: List<TodoModel>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
        requireActivity().nextFragment(
            R.id.action_todoListFragment_to_addTodoFragment,
            bundle = bundle
        )
    }

    private fun deleteTodo(todoId: Int?) {
        todoId?.let {
            viewModel.deleteTodo(it).observe(this, { state ->
                when (state) {
                    is GeneralState.Loading -> showHideViews(showLoader = true, showRecycler = true)
                    is GeneralState.Error -> {
                        showHideViews(showLoader = false, showRecycler = true)
                        showToast(state.message)
                    }
                    is GeneralState.Success<*> -> {
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