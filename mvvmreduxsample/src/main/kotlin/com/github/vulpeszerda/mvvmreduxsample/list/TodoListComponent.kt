package com.github.vulpeszerda.mvvmreduxsample.list

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoListComponent(activity: TodoListActivity) {

    val contextDelegate: ContextDelegate by lazy {
        ContextDelegate.create(activity)
    }

    val stateView: TodoListStateView by lazy {
        TodoListStateView(contextDelegate, extraHandler, viewModel)
    }

    val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(contextDelegate, viewModel)
    }

    val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(
            activity,
            TodoListViewModel.createFactory(
                GlobalState(TodoListState()),
                { extraHandler },
                TodoDatabase.getInstance(activity)
            )
        ).get(TodoListViewModel::class.java)
    }

}