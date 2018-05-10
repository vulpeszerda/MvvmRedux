package com.vulpeszerda.mvvmredux.sample.create

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
class TodoCreateInjection(
    activity: TodoCreateActivity
) : BaseComponent<TodoCreateState>(ActivityContextService(activity)) {


    override val stateView: TodoCreateStateView by lazy {
        TodoCreateStateView(contextService)
    }

    override val extraHandler: TodoCreateExtraHandler by lazy {
        TodoCreateExtraHandler(contextService)
    }

    override val viewModel: TodoCreateViewModel by lazy {
        ViewModelProvider(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoCreateViewModel::class.java)
    }

    val binder: ReduxBinder by lazy {
        ReduxBinder.SimpleImpl(this) { GlobalState(TodoCreateState()) }
    }

}