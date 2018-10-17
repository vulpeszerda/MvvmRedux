package com.github.vulpeszerda.mvvmreduxsample.detail

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoDetailComponent(
    activity: TodoDetailActivity,
    uid: Long
) {

    val contextDelegate: ContextDelegate by lazy {
        ContextDelegate.create(activity)
    }

    val extraHandler: TodoDetailExtraHandler by lazy {
        TodoDetailExtraHandler(contextDelegate)
    }

    val stateView: TodoDetailStateView by lazy {
        TodoDetailStateView(contextDelegate, viewModel)
    }

    val viewModel: TodoDetailViewModel by lazy {
        ViewModelProvider(
            activity,
            TodoDetailViewModel.createFactory(
                GlobalState(TodoDetailState(uid)),
                { extraHandler },
                TodoDatabase.getInstance(activity)
            )
        ).get(TodoDetailViewModel::class.java)
    }

}