package com.vulpeszerda.mvvmredux.sample.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateActivity : ReduxActivity() {

    private val injection: TodoCreateInjection by lazy {
        TodoCreateInjection(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_create)
        setupViewModel(savedInstanceState)

        lifecycle.addObserver(injection.stateView)

        injection.viewModel.apply {
            injection.navigator.subscribe(navigation)
            injection.extraHandler.subscribe(extra)
            injection.errorHandler.subscribe(error)
            injection.stateView.subscribe(state)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupViewModel(savedInstanceState)
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        injection.viewModel.initialize(
            GlobalState(
                restoreStateFromBundle(savedInstanceState)
            ),
            Observable.empty<ReduxEvent>()
                .mergeWith(injection.stateView.events)
                .mergeWith(injection.navigator.events)
                .mergeWith(injection.errorHandler.events)
                .mergeWith(injection.extraHandler.events)
        )
    }

    private fun restoreStateFromBundle(bundle: Bundle?): TodoCreateState {
        return TodoCreateState()
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}
