package com.vulpeszerda.mvvmredux.sample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vulpeszerda.mvvmredux.ReduxEventPublisher
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable

class TodoListActivity : AppCompatActivity() {

    private val component: TodoListComponent by lazy {
        TodoListComponent(this)
    }

    private val publisher : ReduxEventPublisher by lazy {
        ReduxEventPublisher.Impl()
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_list)

        with(component) {
            with(lifecycle) {
                addObserver(stateView)
                addObserver(extraHandler)
            }

            viewModel.initialize(
                GlobalState(TodoListState()),
                Observable.mergeArray(
                    stateView.events,
                    extraHandler.events,
                    publisher.events
                )
            )

            stateView.subscribe(viewModel.state)
            extraHandler.subscribe(viewModel.extra)
        }
    }

    override fun onStart() {
        super.onStart()
        publisher.publishEvent(TodoListEvent.Refresh(!firstLoading))
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, TodoListActivity::class.java)
    }
}
