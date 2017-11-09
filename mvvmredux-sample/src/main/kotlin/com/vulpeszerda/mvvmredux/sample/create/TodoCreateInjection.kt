package com.vulpeszerda.mvvmredux.sample.create

import android.arch.lifecycle.ViewModelProviders
import com.vulpeszerda.mvvmredux.ReduxEvent
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
        TodoCreateStateView(activity)
    }

    val navigator: TodoCreateNavigator by lazy {
        TodoCreateNavigator(activity)
    }

    val extraHandler: TodoCreateExtraHandler by lazy {
        TodoCreateExtraHandler(activity)
    }

    val viewModel: TodoCreateViewModel by lazy {
        ViewModelProviders.of(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
                .get(TodoCreateViewModel::class.java)
    }

}