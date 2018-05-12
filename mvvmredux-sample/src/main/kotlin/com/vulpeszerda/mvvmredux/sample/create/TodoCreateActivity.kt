package com.vulpeszerda.mvvmredux.sample.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxBinder
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateActivity : ReduxActivity() {

    private val component: TodoCreateComponent by lazy {
        TodoCreateComponent(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_create)
        ReduxBinder.bind(component, GlobalState(TodoCreateState()), events)
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}
