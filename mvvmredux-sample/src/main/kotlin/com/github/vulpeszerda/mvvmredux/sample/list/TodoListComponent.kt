package com.github.vulpeszerda.mvvmredux.sample.list

import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.sample.BaseComponent
import com.github.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.github.vulpeszerda.mvvmredux.sample.database.TodoDatabase

class TodoListComponent(
    activity: TodoListActivity
) : BaseComponent<TodoListState>(ContextDelegate.create(activity)) {

    override val stateView: TodoListStateView by lazy {
        TodoListStateView(contextDelegate)
    }

    override val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(contextDelegate)
    }

    override val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoListViewModel::class.java)
    }

}