package com.vulpeszerda.mvvmredux.sample.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxBinder
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.R

class TodoDetailActivity : ReduxActivity() {

    private val component: TodoDetailComponent by lazy {
        TodoDetailComponent(this)
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail)
        val initialState = GlobalState(TodoDetailState(intent.getLongExtra(EXTRA_UID, -1)))
        ReduxBinder.bind(component, initialState, events)
    }

    override fun onStart() {
        super.onStart()
        component.viewModel.stateStore?.run {
            publishEvent(TodoDetailEvent.Refresh(latest.subState.todoUid, !firstLoading))
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
