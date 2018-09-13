package com.vulpeszerda.mvvmredux.sample.detail

import android.arch.lifecycle.ViewModelProvider
import com.vulpeszerda.mvvmredux.ActivityContextService
import com.vulpeszerda.mvvmredux.sample.BaseComponent
import com.vulpeszerda.mvvmredux.sample.ViewModelFactory
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase

class TodoDetailComponent(
    activity: TodoDetailActivity
) : BaseComponent<TodoDetailState>(ActivityContextService(activity)) {

    override val extraHandler: TodoDetailExtraHandler by lazy {
        TodoDetailExtraHandler(contextService)
    }

    override val stateView: TodoDetailStateView by lazy {
        TodoDetailStateView(contextService)
    }

    override val viewModel: TodoDetailViewModel by lazy {
        ViewModelProvider(activity, ViewModelFactory(TodoDatabase.getInstance(activity)))
            .get(TodoDetailViewModel::class.java)
    }

}