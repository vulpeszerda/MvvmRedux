package com.vulpeszerda.mvvmredux.sample.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vulpeszerda.mvvmredux.ReduxEventPublisher
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.Observable

class TodoDetailActivity : AppCompatActivity() {

    private val component: TodoDetailComponent by lazy {
        TodoDetailComponent(this)
    }

    private val publisher : ReduxEventPublisher by lazy {
        ReduxEventPublisher.Impl()
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail)

        with(component) {
            with(lifecycle) {
                addObserver(stateView)
                addObserver(extraHandler)
            }

            viewModel.initialize(
                GlobalState(TodoDetailState(intent.getLongExtra(EXTRA_UID, -1))),
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
        component.viewModel.stateStore?.run {
            publisher.publishEvent(TodoDetailEvent.Refresh(latest.subState.todoUid, !firstLoading))
        }
    }

    override fun onStop() {
        firstLoading = false
        super.onStop()
    }

    companion object {

        const val EXTRA_UID = "EXTRA_UID"

        fun createIntent(context: Context, uid: Long): Intent {
            return Intent(context, TodoDetailActivity::class.java)
                .apply { putExtra(EXTRA_UID, uid) }
        }
    }

}
