package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.ViewModelProviders
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoListInjection(private val activity: TodoListActivity) {

    val errorHandler: TodoListErrorHandler by lazy {
        TodoListErrorHandler(activity)
    }

    val stateView: TodoListStateView by lazy {
        TodoListStateView(activity)
    }

    val navigator: TodoListNavigator by lazy {
        TodoListNavigator(activity)
    }

    val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(activity)
    }

    val viewModel: TodoListViewModel by lazy {
        ViewModelProviders.of(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoListViewModel::class.java)
    }
}