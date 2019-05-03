package com.github.vulpeszerda.mvvmreduxsample.detail

import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseStateView
import kotlinx.android.synthetic.main.todo_detail.message as viewMessage
import kotlinx.android.synthetic.main.todo_detail.title as viewTitle

class TodoDetailStateView(
    contextDelegate: ContextDelegate
) : BaseStateView<TodoDetailState>("TodoDetailStateView", contextDelegate) {

    init {
        addConsumer({ it.subState.todo }) { _, curr ->
            viewTitle.text = curr.subState.todo?.title
            viewMessage.text = curr.subState.todo?.message
        }
        addConsumer({ it.subState.loading }) { _, curr ->
            if (curr.subState.loading) {
                showProgressDialog("Loading..")
            } else {
                hideProgressDialog()
            }
        }
    }
}