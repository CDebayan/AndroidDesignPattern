package com.dc.todomvvmretrofitcoroutinedagger.utils

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
import com.dc.todomvvmretrofitcoroutinedagger.R
import com.dc.todomvvmretrofitcoroutinedagger.base.GeneralError
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

fun Context.connectionCheck(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun Context.getToken(): String? {
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(USER_CREDENTIAL, Context.MODE_PRIVATE)
    return sharedPreferences.getString("apiToken", null)
}

fun Context.setToken(token: String?) {
    if (token != null) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(
            USER_CREDENTIAL, Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("apiToken", token)
        editor.apply()
        editor.commit()
    }
}

fun Context.clearToken() {
    val sharedPreferences: SharedPreferences = getSharedPreferences(
        USER_CREDENTIAL, Context.MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("apiToken", null)
    editor.apply()
    editor.commit()

}

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

fun checkConnectivityError(t: Throwable): GeneralError {
    return when (t) {
        is ConnectException -> {
            GeneralError(-1, "No Internet Connection")
        }
        is SocketTimeoutException -> {
            GeneralError(-2, "Cannot connect to server")

        }
        else -> {
            GeneralError(0, "Something went wrong")
        }
    }
}