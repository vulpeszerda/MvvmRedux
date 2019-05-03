package com.github.vulpeszerda.mvvmreduxsample.create

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseComponent
import com.github.vulpeszerda.mvvmreduxsample.ViewModelFactory
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoCreateComponent(
    activity: TodoCreateActivity
) : BaseComponent<TodoCreateState>(ContextDelegate.create(activity)) {


    override val stateView: TodoCreateStateView by lazy {
        TodoCreateStateView(contextDelegate)
    }

    override val extraHandler: TodoCreateExtraHandler by lazy {
        TodoCreateExtraHandler(contextDelegate)
    }

    override val viewModel: TodoCreateViewModel by lazy {
        ViewModelProvider(
            activity,
            ViewModelFactory(
                TodoDatabase.getInstance(activity)
            )
        )
            .get(TodoCreateViewModel::class.java)
    }

}