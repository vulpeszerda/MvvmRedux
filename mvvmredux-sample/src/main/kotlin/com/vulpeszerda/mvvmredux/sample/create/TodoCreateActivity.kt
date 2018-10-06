package com.vulpeszerda.mvvmredux.sample.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable

class TodoCreateActivity : AppCompatActivity() {

    private val component: TodoCreateComponent by lazy {
        TodoCreateComponent(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_create)

        with(component) {
            with(lifecycle) {
                addObserver(stateView)
                addObserver(extraHandler)
            }

            viewModel.initialize(
                GlobalState(TodoCreateState()),
                Observable.mergeArray(
                    stateView.events,
                    extraHandler.events
                )
            )

            stateView.subscribe(viewModel.state)
            extraHandler.subscribe(viewModel.extra)
        }
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}
