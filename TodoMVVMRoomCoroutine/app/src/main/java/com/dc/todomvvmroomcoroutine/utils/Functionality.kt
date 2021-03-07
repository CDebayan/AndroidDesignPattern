package com.dc.todomvvmroomcoroutine.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dc.todomvvmroomcoroutine.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

fun Activity.nextFragment(id: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this, R.id.hostFragment).navigate(id, bundle)
}

fun Activity.popFragment() {
    Navigation.findNavController(this, R.id.hostFragment).popBackStack()
}

fun Fragment.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun convertDateTime(dateTime: String?): String {
    try {
        dateTime?.let {
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
            dateFormat.parse(it)?.let {
                return SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.ENGLISH).format(it)
            }
        }
    } catch (e: Exception) {
    }
    return ""
}
