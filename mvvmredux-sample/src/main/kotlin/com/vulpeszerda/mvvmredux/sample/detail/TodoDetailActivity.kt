package com.vulpeszerda.mvvmredux.sample.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.sample.R
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoDetailActivity : ReduxActivity() {

    private val injection: TodoDetailInjection by lazy {
        TodoDetailInjection(this)
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail)
        injection.binder.setupViewModel(savedInstanceState, events)
    }

    override fun onStart() {
        super.onStart()
        injection.viewModel.stateStore?.run {
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
