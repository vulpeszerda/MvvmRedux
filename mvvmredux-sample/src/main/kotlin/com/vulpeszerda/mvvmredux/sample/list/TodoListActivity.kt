package com.vulpeszerda.mvvmredux.sample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class TodoListActivity : ReduxActivity() {

    private val injection: TodoListInjection by lazy {
        TodoListInjection(this)
    }

    private val eventSubject = PublishSubject.create<TodoListEvent>()

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_list)
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
        injection.viewModel.initialize(GlobalState(
                restoreStateFromBundle(savedInstanceState)),
                Observable.empty<ReduxEvent>()
                        .mergeWith(eventSubject)
                        .mergeWith(injection.stateView.events)
                        .mergeWith(injection.navigator.events)
                        .mergeWith(injection.errorHandler.events)
                        .mergeWith(injection.extraHandler.events))
    }

    private fun restoreStateFromBundle(bundle: Bundle?): TodoListState {
        return TodoListState()
    }

    override fun onStart() {
        super.onStart()
        eventSubject.onNext(TodoListEvent.Refresh(!firstLoading))
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, TodoListActivity::class.java)
    }
}
