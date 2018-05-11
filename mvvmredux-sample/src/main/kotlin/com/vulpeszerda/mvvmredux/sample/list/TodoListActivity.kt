package com.vulpeszerda.mvvmredux.sample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxBinder
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R

class TodoListActivity : ReduxActivity() {

    private val component: TodoListComponent by lazy {
        TodoListComponent(this)
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_list)
        ReduxBinder.bind(component, GlobalState(TodoListState()), events)
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
