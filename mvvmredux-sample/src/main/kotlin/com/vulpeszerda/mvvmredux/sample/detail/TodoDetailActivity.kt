package com.vulpeszerda.mvvmredux.sample.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoDetailActivity : ReduxActivity() {

    private val injection: TodoDetailInjection by lazy {
        TodoDetailInjection(this)
    }

    private val eventSubject = PublishSubject.create<TodoDetailEvent>()
    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail)
        setupViewModel(savedInstanceState)

        lifecycle.addObserver(injection.stateView)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupViewModel(savedInstanceState)
    }

    private fun setupViewModel(savedInstanceState: Bundle?) {
        injection.viewModel.initialize(GlobalState(
                restoreStateFromBundle(savedInstanceState)),
                Observable.empty<TodoDetailEvent>()
                        .mergeWith(eventSubject)
                        .mergeWith(injection.stateView.events)
                        .mergeWith(injection.errorHandler.events)
                        .mergeWith(injection.extraHandler.events))
    }

    private fun restoreStateFromBundle(bundle: Bundle?): TodoDetailState {
        return TodoDetailState(intent.getLongExtra(EXTRA_UID, -1))
    }

    override fun onStart() {
        super.onStart()
        injection.viewModel.stateStore?.run {
            eventSubject.onNext(TodoDetailEvent.Refresh(latest.subState.todoUid, !firstLoading))
        }
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        private const val EXTRA_UID = "EXTRA_UID"

        fun createIntent(context: Context, uid: Long): Intent {
            return Intent(context, TodoDetailActivity::class.java)
                    .apply { putExtra(EXTRA_UID, uid) }
        }
    }

}
