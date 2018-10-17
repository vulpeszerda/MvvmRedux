package com.github.vulpeszerda.mvvmreduxsample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.vulpeszerda.mvvmreduxsample.R

class TodoListActivity : AppCompatActivity() {

    private val component: TodoListComponent by lazy {
        TodoListComponent(this)
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_list)

        lifecycle.addObserver(component.stateView)
        lifecycle.addObserver(component.extraHandler)
    }

    override fun onStart() {
        super.onStart()
        component.viewModel.refresh()
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, TodoListActivity::class.java)
    }
}
