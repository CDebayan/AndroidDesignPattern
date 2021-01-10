package com.dc.todomvvmretrofit.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dc.todomvvmretrofit.R
import com.dc.todomvvmretrofit.utils.getToken
import com.dc.todomvvmretrofit.utils.openActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if(getToken() != null){
                openActivity(TodoListActivity::class.java,clearTask = true)
            }else{
                openActivity(LoginActivity::class.java,clearTask = true)
            }
        }, 2000)
    }
}