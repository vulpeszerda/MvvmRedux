package com.github.vulpeszerda.mvvmreduxsample.detail

import com.github.vulpeszerda.mvvmreduxsample.BaseStateView
import com.github.vulpeszerda.mvvmreduxsample.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.StateConsumer
import kotlinx.android.synthetic.main.todo_detail.message as viewMessage
import kotlinx.android.synthetic.main.todo_detail.title as viewTitle

class TodoDetailStateView(
    contextDelegate: ContextDelegate
) : BaseStateView<TodoDetailState>("TodoDetailStateView", contextDelegate) {

    init {
        addConsumer(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev.subState.todo != curr.subState.todo },
                apply = { _, curr ->
                    viewTitle.text = curr.subState.todo?.title
                    viewMessage.text = curr.subState.todo?.message
                })
        )
        addConsumer(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev.subState.loading != curr.subState.loading },
                apply = { _, curr ->
                    if (curr.subState.loading) {
                        showProgressDialog("Loading..")
                    } else {
                        hideProgressDialog()
                    }
                })
        )
    }
}