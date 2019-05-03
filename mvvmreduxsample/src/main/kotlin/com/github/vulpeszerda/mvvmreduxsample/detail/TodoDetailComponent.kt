package com.github.vulpeszerda.mvvmreduxsample.detail

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseComponent
import com.github.vulpeszerda.mvvmreduxsample.ViewModelFactory
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoDetailComponent(
    activity: TodoDetailActivity
) : BaseComponent<TodoDetailState>(ContextDelegate.create(activity)) {

    override val extraHandler: TodoDetailExtraHandler by lazy {
        TodoDetailExtraHandler(contextDelegate)
    }

    override val stateView: TodoDetailStateView by lazy {
        TodoDetailStateView(contextDelegate)
    }

    override val viewModel: TodoDetailViewModel by lazy {
        ViewModelProvider(
            activity,
            ViewModelFactory(
                TodoDatabase.getInstance(activity)
            )
        )
            .get(TodoDetailViewModel::class.java)
    }

}