package com.vulpeszerda.mvvmredux.sample.detail

import android.arch.lifecycle.ViewModelProviders
import com.vulpeszerda.mvvmredux.library.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailInjection(private val activity: TodoDetailActivity) {

    val errorHandler: TodoDetailErrorHandler by lazy {
        TodoDetailErrorHandler(activity)
    }

    val extraHandler: TodoDetailExtraHandler by lazy {
        TodoDetailExtraHandler(activity) { errorHandler.onError(ReduxEvent.Error(it)) }
    }

    val stateView: TodoDetailStateView by lazy {
        TodoDetailStateView(activity) { errorHandler.onError(ReduxEvent.Error(it)) }
    }

    val viewModel: TodoDetailViewModel by lazy {
        ViewModelProviders.of(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
                .get(TodoDetailViewModel::class.java)
                .apply {
                    extraHandler.subscribe(extra)
                    errorHandler.subscribe(error)
                    stateView.subscribe(state)
                }
    }

}