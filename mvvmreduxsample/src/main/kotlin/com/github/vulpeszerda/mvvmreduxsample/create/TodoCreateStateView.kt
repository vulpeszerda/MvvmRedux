package com.github.vulpeszerda.mvvmreduxsample.create

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseStateView
import kotlinx.android.synthetic.main.todo_create.*
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

class TodoCreateStateView(
    contextDelegate: ContextDelegate,
    private val viewModel: TodoCreateViewModel
) : BaseStateView("TodoCreateStateView", contextDelegate) {

    init {
        viewModel.subscribe({ it.subState.loading }) { _, curr ->
            if (curr.subState.loading) {
                showProgressDialog("Loading")
            } else {
                hideProgressDialog()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        btn_save.setOnClickListener {
            val title = viewTitle.text?.toString() ?: return@setOnClickListener
            val message = viewMessage.text?.toString() ?: return@setOnClickListener
            viewModel.save(title, message)
        }
    }
}