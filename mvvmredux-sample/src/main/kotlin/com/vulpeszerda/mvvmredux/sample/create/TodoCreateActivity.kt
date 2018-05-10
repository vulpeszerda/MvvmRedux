package com.vulpeszerda.mvvmredux.sample.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.sample.R
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateActivity : ReduxActivity() {

    private val injection: TodoCreateInjection by lazy {
        TodoCreateInjection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_create)
        injection.binder.setupViewModel(savedInstanceState, events)
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}
