package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.ViewModelProviders
import com.vulpeszerda.mvvmredux.library.ReduxEvent
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
        TodoListStateView(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val navigator: TodoListNavigator by lazy {
        TodoListNavigator(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val extraHandler: TodoListExtraHandler by lazy {
        TodoListExtraHandler(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val viewModel: TodoListViewModel by lazy {
        ViewModelProviders.of(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
                .get(TodoListViewModel::class.java)
                .apply {
                    navigator.subscribe(navigation)
                    extraHandler.subscribe(extra)
                    errorHandler.subscribe(error)
                    stateView.subscribe(state)
                }
    }
}