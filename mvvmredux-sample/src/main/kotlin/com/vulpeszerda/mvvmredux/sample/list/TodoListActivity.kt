package com.vulpeszerda.mvvmredux.sample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.sample.R

class TodoListActivity : ReduxActivity() {

    private val injection: TodoListInjection by lazy {
        TodoListInjection(this)
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_list)
        injection.binder.setupViewModel(savedInstanceState, events)
    }

    override fun onStart() {
        super.onStart()
        publishEvent(TodoListEvent.Refresh(!firstLoading))
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, TodoListActivity::class.java)
    }
}
