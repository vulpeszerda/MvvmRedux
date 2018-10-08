package com.github.vulpeszerda.mvvmreduxsample.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.R
import com.github.vulpeszerda.mvvmreduxsample.ReduxEventPublisher
import io.reactivex.Observable

class TodoListActivity : AppCompatActivity() {

    private val component: TodoListComponent by lazy {
        TodoListComponent(this)
    }

    private val publisher: ReduxEventPublisher by lazy {
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
