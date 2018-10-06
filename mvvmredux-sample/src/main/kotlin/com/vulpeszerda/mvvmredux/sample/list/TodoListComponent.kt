package com.vulpeszerda.mvvmredux.sample.list

import androidx.lifecycle.ViewModelProvider
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.sample.BaseComponent
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

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