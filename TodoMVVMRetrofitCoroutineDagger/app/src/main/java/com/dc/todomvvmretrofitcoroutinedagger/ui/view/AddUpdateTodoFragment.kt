package com.dc.todomvvmretrofitcoroutinedagger.ui.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofitcoroutinedagger.R
import com.dc.todomvvmretrofitcoroutinedagger.base.BaseFragment
import com.dc.todomvvmretrofitcoroutinedagger.base.ViewModelProviderFactory
import com.dc.todomvvmretrofitcoroutinedagger.data.model.TodoModel
import com.dc.todomvvmretrofitcoroutinedagger.databinding.FragmentAddUpdateTodoBinding
import com.dc.todomvvmretrofitcoroutinedagger.ui.viewmodel.AddUpdateTodoViewModel
import com.dc.todomvvmretrofitcoroutinedagger.utils.*
import com.dc.todomvvmretrofitcoroutinedagger.utils.custombottomsheet.BottomSheetModel
import com.dc.todomvvmretrofitcoroutinedagger.utils.custombottomsheet.CustomBottomSheet
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class AddUpdateTodoFragment : BaseFragment() {
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private var type: String? = ""
    private var todoId: Int? = null
    private lateinit var binding: FragmentAddUpdateTodoBinding
    private var selectedPriority: String = ""
    private var selectedDateTime: String = ""

    private val viewModel: AddUpdateTodoViewModel by lazy {
        ViewModelProvider(
            this,
            providerFactory
        ).get(AddUpdateTodoViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments?.getString("type")
    }

    override fun getChildView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ChildView {
        binding = FragmentAddUpdateTodoBinding.inflate(inflater, container, false)
        return when (type) {
            "add" -> {
                ChildView(
                    view = binding.root,
                    title = "Add Todo",
                    showBack = true
                )
            }
            "update" -> {
                ChildView(
                    view = binding.root,
                    title = "Update Todo",
                    showBack = true
                )
            }
            else -> {
                ChildView(
                    view = binding.root,
                    title = "",
                    showBack = true
                )
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataToFields(arguments?.getParcelable("todoData"))
        setViews()
        onClickListener()
    }

    @SuppressLint("SetTextI18n")
    private fun setViews() {
        type?.let {
            if (it == "add") {
                binding.addUpdateButton.text = "Add"
            } else if (it == "update") {
                binding.addUpdateButton.text = "Update"
            }
        }
    }

    private fun setDataToFields(todoData: TodoModel?) {
        todoData?.let {
            it.todoId?.let { todoId ->
                this.todoId = todoId
            }
            it.title?.let { title ->
                binding.title.setText(title)
            }
            it.description?.let { description ->
                binding.description.setText(description)
            }
            it.dateTime?.let { dateTime ->
                selectedDateTime = dateTime
                binding.datetime.setText(dateTime)
            }
            it.priority?.let { priority ->
                selectedPriority = priority
                binding.priority.setText(priority)
            }
        }
    }

    private fun onClickListener() {
        binding.datetime.setOnClickListener {
            pickDateTime()
        }
        binding.datetime.setOnFocusChangeListener { _, b ->
            if (b) {
                pickDateTime()
            }
        }

        binding.priority.setOnClickListener {
            showPriorityBottomSheet()
        }
        binding.priority.setOnFocusChangeListener { _, b ->
            if (b) {
                showPriorityBottomSheet()
            }
        }

        binding.addUpdateButton.setOnClickListener {
            addUpdateTodo()
        }

        binding.title.addTextChangedListener {
            if (binding.titleLayout.error != null) {
                binding.titleLayout.error = null
            }
        }
        binding.description.addTextChangedListener {
            if (binding.descriptionLayout.error != null) {
                binding.descriptionLayout.error = null
            }
        }
        binding.datetime.addTextChangedListener {
            if (binding.datetimeLayout.error != null) {
                binding.datetimeLayout.error = null
            }
        }
        binding.priority.addTextChangedListener {
            if (binding.priorityLayout.error != null) {
                binding.priorityLayout.error = null
            }
        }

    }

    private fun addUpdateTodo() {
        val title: String = binding.title.text.toString().trim()
        val description: String = binding.description.text.toString().trim()
        val dateTime: String = selectedDateTime
        val priority: String = selectedPriority

        if (type == "add") {
            viewModel.addTodo(
                title = title,
                description = description,
                dateTime = dateTime,
                priority = priority
            ).observe(viewLifecycleOwner, ::handleState)
        } else if (type == "update") {
            todoId?.let {
                viewModel.updateTodo(
                    todoId = it,
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    priority = priority
                ).observe(viewLifecycleOwner, ::handleState)
            }
        }
    }

    private fun handleState(state: AddUpdateTodoViewModel.State) {
        when (state) {
            is AddUpdateTodoViewModel.State.Loading -> setLoading(true)
            is AddUpdateTodoViewModel.State.Error -> {
                setLoading(false)
                showToast(state.message)
            }
            is AddUpdateTodoViewModel.State.ValidationError -> {
                setLoading(false)
                binding.titleLayout.error = state.titleError
                binding.descriptionLayout.error = state.descriptionError
                binding.datetimeLayout.error = state.dateTimeError
                binding.priorityLayout.error = state.priorityError
            }
            is AddUpdateTodoViewModel.State.Success -> {
                setLoading(false)
                showToast(state.message)
                requireActivity().popFragment()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.titleLayout.disable()
            binding.descriptionLayout.disable()
            binding.datetimeLayout.disable()
            binding.priorityLayout.disable()
            binding.progressBar.show()
            binding.addUpdateButton.disable()
            binding.addUpdateButton.invisible()

        } else {
            binding.titleLayout.enable()
            binding.descriptionLayout.enable()
            binding.datetimeLayout.enable()
            binding.priorityLayout.enable()
            binding.progressBar.gone()
            binding.addUpdateButton.enable()
            binding.addUpdateButton.show()
        }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                setDateTime(pickedDateTime)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setDateTime(pickedDateTime: Calendar) {
        selectedDateTime = pickedDateTime.time.toString()
        binding.datetime.setText(convertDateTime(selectedDateTime))
    }

    private fun showPriorityBottomSheet() {
        val list = ArrayList<BottomSheetModel>()

        list.add(BottomSheetModel(R.drawable.ic_circle_red, "High"))
        list.add(BottomSheetModel(R.drawable.ic_circle_yellow, "Medium"))
        list.add(BottomSheetModel(R.drawable.ic_circle_green, "Low"))

        CustomBottomSheet(requireContext(), list).setOnClickListener(object :
            CustomBottomSheet.BottomSheetClickListener {
            override fun onClick(model: BottomSheetModel) {
                selectedPriority = model.name
                binding.priority.setText(model.name)
            }
        }).show()
    }


}