package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.ViewModelProvider
import com.vulpeszerda.mvvmredux.ActivityContextService
import com.vulpeszerda.mvvmredux.ReduxBinder
import com.vulpeszerda.mvvmredux.sample.BaseComponent
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoListComponent(
    activity: TodoListActivity
) : BaseComponent<TodoListState>(ActivityContextService(activity)) {

    override val stateView: TodoListStateView by lazy {
        TodoListStateView(contextService)
    }

    override val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(contextService)
    }

    override val viewModel: TodoListViewModel by lazy {
        ViewModelProvider(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoListViewModel::class.java)
    }

}