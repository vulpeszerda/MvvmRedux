package com.vulpeszerda.mvvmredux.sample.create

import android.arch.lifecycle.ViewModelProviders
import com.vulpeszerda.mvvmredux.library.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoCreateInjection(private val activity: TodoCreateActivity) {


    val errorHandler: TodoCreateErrorHandler by lazy {
        TodoCreateErrorHandler(activity)
    }

    val stateView: TodoCreateStateView by lazy {
        TodoCreateStateView(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val navigator: TodoCreateNavigator by lazy {
        TodoCreateNavigator(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val extraHandler: TodoCreateExtraHandler by lazy {
        TodoCreateExtraHandler(activity) {
            errorHandler.onError(ReduxEvent.Error(it))
        }
    }

    val viewModel: TodoCreateViewModel by lazy {
        ViewModelProviders.of(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
                .get(TodoCreateViewModel::class.java)
                .apply {
                    navigator.subscribe(navigation)
                    extraHandler.subscribe(extra)
                    errorHandler.subscribe(error)
                    stateView.subscribe(state)
                }
    }

}