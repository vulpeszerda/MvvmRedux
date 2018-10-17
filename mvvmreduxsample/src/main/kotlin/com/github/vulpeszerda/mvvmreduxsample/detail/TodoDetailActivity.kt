package com.github.vulpeszerda.mvvmreduxsample.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.vulpeszerda.mvvmreduxsample.R

class TodoDetailActivity : AppCompatActivity() {

    private val component: TodoDetailComponent by lazy {
        TodoDetailComponent(this, intent.getLongExtra(EXTRA_UID, -1))
    }

    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_detail)

        lifecycle.addObserver(component.stateView)
        lifecycle.addObserver(component.extraHandler)
    }

    override fun onStart() {
        super.onStart()
        component.viewModel.refresh()
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
