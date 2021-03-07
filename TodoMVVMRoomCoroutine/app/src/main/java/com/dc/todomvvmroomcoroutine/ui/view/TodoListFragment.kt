package com.dc.todomvvmroomcoroutine.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.todomvvmroomcoroutine.R
import com.dc.todomvvmroomcoroutine.base.BaseFragment
import com.dc.todomvvmroomcoroutine.base.ChildView
import com.dc.todomvvmroomcoroutine.data.local.AppDatabase
import com.dc.todomvvmroomcoroutine.data.model.TodoModel
import com.dc.todomvvmroomcoroutine.data.repository.TodoRepository
import com.dc.todomvvmroomcoroutine.databinding.FragmentTodoListBinding
import com.dc.todomvvmroomcoroutine.ui.adapter.TodoListAdapter
import com.dc.todomvvmroomcoroutine.ui.viewmodel.TodoListViewModel
import com.dc.todomvvmroomcoroutine.utils.*

class TodoListFragment : BaseFragment() {
    private lateinit var binding: FragmentTodoListBinding

    private val adapter = TodoListAdapter()
    private val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(
                this,
                TodoListViewModel.ViewModelFactory(
                        TodoRepository.instance(
                                AppDatabase.invoke(requireContext()).appDAO()
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
        binding.addButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "add")
            requireActivity().nextFragment(
                    R.id.action_todoListFragment_to_addUpdateTodoFragment,
                    bundle = bundle
            )
        }

    }

    private fun observers() {
        viewModel.todoList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { data, option ->
            when (option) {
                RecyclerViewOption.Delete -> {
                    deleteTodo(data)
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
                R.id.action_todoListFragment_to_addUpdateTodoFragment,
                bundle = bundle
        )
    }

    private fun deleteTodo(todoModel: TodoModel) {

        viewModel.deleteTodo(todoModel).observe(viewLifecycleOwner, { state ->
            when (state) {
                is GeneralState.Loading -> showHideViews(showLoader = true, showRecycler = true)
                is GeneralState.Error -> {
                    showHideViews(showRecycler = true)
                    showToast(state.message)
                }
                is GeneralState.Success -> {
                    showHideViews(showRecycler = true)
                    showToast(state.message)
                }
            }
        })

    }

    private fun showHideViews(
            showRecycler: Boolean = false,
            showLoader: Boolean = false
    ) {
        if (showRecycler) {
            binding.recyclerView.show()
        } else {
            binding.recyclerView.gone()
        }

        if (showLoader) {
            binding.progress.show()
        } else {
            binding.progress.gone()
        }
    }

}