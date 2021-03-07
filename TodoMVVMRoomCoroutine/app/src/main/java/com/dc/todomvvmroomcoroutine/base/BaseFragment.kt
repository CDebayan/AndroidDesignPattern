package com.dc.todomvvmroomcoroutine.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dc.todomvvmroomcoroutine.databinding.FragmentBaseBinding
import com.dc.todomvvmroomcoroutine.utils.*

abstract class BaseFragment : Fragment() {

    private lateinit var baseBinding: FragmentBaseBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        val fragmentDetail = getChildView(inflater, container, savedInstanceState)
        setTitle(fragmentDetail.title)
        enableDisableBack(fragmentDetail.showBack)
        baseBinding.baseLayout.addView(fragmentDetail.view)
        return baseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseClickListener()
    }

    private fun setTitle(title: String?) {
        baseBinding.baseTitle.text = title
    }

    private fun enableDisableBack(showBack: Boolean) {
        if (showBack) {
            baseBinding.backButton.show()
            baseBinding.backButton.enable()
        } else {
            baseBinding.backButton.invisible()
            baseBinding.backButton.disable()
        }
    }

    private fun baseClickListener() {
        baseBinding.backButton.setOnClickListener {
            requireActivity().popFragment()
        }
    }

    abstract fun getChildView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): ChildView

}