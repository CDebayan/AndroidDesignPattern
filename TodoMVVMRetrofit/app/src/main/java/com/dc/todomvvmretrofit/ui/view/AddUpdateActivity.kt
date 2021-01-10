package com.dc.todomvvmretrofit.ui.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dc.todomvvmretrofit.R
import com.dc.todomvvmretrofit.utils.custombottomsheet.BottomSheetModel
import com.dc.todomvvmretrofit.utils.custombottomsheet.CustomBottomSheet
import com.dc.todomvvmretrofit.databinding.ActivityAddUpdateBinding
import com.dc.todomvvmretrofit.data.model.TodoModel
import com.dc.todomvvmretrofit.ui.viewmodel.AddUpdateTodoViewModel
import com.dc.todomvvmretrofit.utils.*
import java.util.*

class AddUpdateActivity : BaseActivity() {

    private var todoId: Int? = null
    private var type: String? = ""
    private var selectedPriority: String = ""
    private var selectedDateTime: String = ""
    private lateinit var addUpdateTodoViewModel: AddUpdateTodoViewModel
    private lateinit var binding: ActivityAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        addUpdateTodoViewModel = ViewModelProvider(this).get(AddUpdateTodoViewModel::class.java)

        getIntentData()

        onClickListener()
    }

    private fun getIntentData() {
        if (intent.hasExtra("bundleData")) {
            val bundleExtra: Bundle? = intent.getBundleExtra("bundleData")
            type = bundleExtra?.getString("type", "")
            val todoData: TodoModel? = bundleExtra?.getParcelable("todoData")
            setViews()
            setDataToFields(todoData)
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

    @SuppressLint("SetTextI18n")
    private fun setViews() {
        type?.let {
            if (it == "add") {
                setBaseView(binding.root, title = "Add Todo", showBack = true)
                binding.addUpdateButton.text = "Add"
            } else if (it == "update") {
                setBaseView(binding.root, title = "Update Todo", showBack = true)
                binding.addUpdateButton.text = "Update"
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
            addUpdateTodoViewModel.addTodo(
                title = title,
                description = description,
                dateTime = dateTime,
                priority = priority
            ).observe(this,::handleState)
        } else if (type == "update") {
            todoId?.let {
                addUpdateTodoViewModel.updateTodo(
                    todoId = it,
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    priority = priority
                ).observe(this,::handleState)
            }
        }
    }

    private fun handleState(state : AddUpdateTodoViewModel.AddUpdateTodoState){
        when(state){
            is AddUpdateTodoViewModel.AddUpdateTodoState.Loading -> setLoading(true)
            is AddUpdateTodoViewModel.AddUpdateTodoState.Error ->{
                setLoading(false)
                showToast(state.message)
            }
            is AddUpdateTodoViewModel.AddUpdateTodoState.ValidationError ->{
                setLoading(false)
                binding.titleLayout.error = state.titleError
                binding.descriptionLayout.error = state.descriptionError
                binding.datetimeLayout.error = state.dateTimeError
                binding.priorityLayout.error = state.priorityError
            }
            is AddUpdateTodoViewModel.AddUpdateTodoState.Success ->{
                setLoading(false)
                showToast(state.message)
                onBackPressed()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading){
            binding.titleLayout.disable()
            binding.descriptionLayout.disable()
            binding.datetimeLayout.disable()
            binding.priorityLayout.disable()
            binding.progressBar.show()
            binding.addUpdateButton.disable()
            binding.addUpdateButton.invisible()

        }else{
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

        DatePickerDialog(this, { _, year, month, day ->
            TimePickerDialog(this, { _, hour, minute ->
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

        CustomBottomSheet(this, list).setOnClickListener(object :
            CustomBottomSheet.BottomSheetClickListener {
            override fun onClick(model: BottomSheetModel) {
                selectedPriority = model.name
                binding.priority.setText(model.name)
            }
        }).show()
    }
}