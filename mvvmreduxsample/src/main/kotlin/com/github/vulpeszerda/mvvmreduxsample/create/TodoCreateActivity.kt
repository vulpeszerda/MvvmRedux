package com.github.vulpeszerda.mvvmreduxsample.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.vulpeszerda.mvvmreduxsample.R

class TodoCreateActivity : AppCompatActivity() {

    private val component: TodoCreateComponent by lazy {
        TodoCreateComponent(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_create)

        lifecycle.addObserver(component.stateView)
        lifecycle.addObserver(component.extraHandler)
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}
