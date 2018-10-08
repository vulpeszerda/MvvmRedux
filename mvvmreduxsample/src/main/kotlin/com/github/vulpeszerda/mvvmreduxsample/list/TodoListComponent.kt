package com.github.vulpeszerda.mvvmreduxsample.list

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmreduxsample.BaseComponent
import com.github.vulpeszerda.mvvmreduxsample.ViewModelFactory
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase

class TodoListComponent(
    activity: TodoListActivity
) : BaseComponent<TodoListState>(com.github.vulpeszerda.mvvmredux.ContextDelegate.create(activity)) {

    override val stateView: TodoListStateView by lazy {
        TodoListStateView(contextDelegate)
    }

    override val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(contextDelegate)
    }

    override val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(
            activity,
            ViewModelFactory(
                TodoDatabase.getInstance(activity)
            )
        )
            .get(TodoListViewModel::class.java)
    }

}