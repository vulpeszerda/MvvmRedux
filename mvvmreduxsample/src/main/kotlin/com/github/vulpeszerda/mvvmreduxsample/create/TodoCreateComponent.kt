package com.github.vulpeszerda.mvvmreduxsample.create

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoCreateComponent(activity: TodoCreateActivity) {

    val contextDelegate: ContextDelegate by lazy {
        ContextDelegate.create(activity)
    }

    val stateView: TodoCreateStateView by lazy {
        TodoCreateStateView(contextDelegate, viewModel)
    }

    val extraHandler: TodoCreateExtraHandler by lazy {
        TodoCreateExtraHandler(contextDelegate)
    }

    val viewModel: TodoCreateViewModel by lazy {
        ViewModelProvider(
            activity,
            TodoCreateViewModel.createFactory(
                GlobalState(TodoCreateState()),
                { extraHandler },
                TodoDatabase.getInstance(activity)
            )
        ).get(TodoCreateViewModel::class.java)
    }

}