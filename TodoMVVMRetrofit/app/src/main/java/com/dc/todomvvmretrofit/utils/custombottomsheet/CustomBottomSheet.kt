package com.dc.todomvvmretrofit.utils.custombottomsheet

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dc.todomvvmretrofit.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.custom_bottom_sheet_child.view.*
import kotlinx.android.synthetic.main.custom_bottom_sheet_parent.view.*

class CustomBottomSheet(
    private val activity: Activity,
    private val list: ArrayList<BottomSheetModel>
) {

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var onClickListener: BottomSheetClickListener

    init {
        setBottomSheetLayout()
    }

    private fun setBottomSheetLayout() {
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.custom_bottom_sheet_parent, null)
        bottomSheetDialog = BottomSheetDialog(activity)
        bottomSheetDialog.setContentView(view)
        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.adapter = RecyclerViewAdapter(list)
    }

    fun show(): CustomBottomSheet {
        if (::bottomSheetDialog.isInitialized) {
            bottomSheetDialog.show()
        }
        return this
    }

    fun setOnClickListener(onClickListener: BottomSheetClickListener): CustomBottomSheet {
        this.onClickListener = onClickListener
        return this
    }

    inner class RecyclerViewAdapter(private val list: ArrayList<BottomSheetModel>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
            return RecyclerViewViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.custom_bottom_sheet_child,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
            holder.setDataToViews(position)
            holder.onClickListener()
        }

        inner class RecyclerViewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun setDataToViews(position: Int) {
                if (list.get(position).image != 0) {
                    itemView.childImage.setImageResource(list.get(position).image)
                    itemView.childImage.visibility = View.VISIBLE
                } else {
                    itemView.childImage.visibility = View.GONE
                }

                itemView.childText.text = list.get(position).name
            }

            fun onClickListener() {
                itemView.childText.setOnClickListener {
                    val bottomSheetModel = list.get(adapterPosition)
                    onClickListener.onClick(bottomSheetModel)
                    bottomSheetDialog.dismiss()
                }
            }

        }
    }

    interface BottomSheetClickListener {
        fun onClick(model: BottomSheetModel)
    }
}

