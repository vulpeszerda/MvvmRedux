package com.github.vulpeszerda.mvvmredux.sample.create

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.sample.BaseComponent
import com.github.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.github.vulpeszerda.mvvmredux.sample.database.TodoDatabase

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
        ViewModelProvider(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoCreateViewModel::class.java)
    }

}