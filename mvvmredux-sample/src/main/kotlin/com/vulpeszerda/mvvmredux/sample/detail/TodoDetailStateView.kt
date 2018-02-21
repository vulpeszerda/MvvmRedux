package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.ActivityContextWrapper
import com.vulpeszerda.mvvmredux.StateConsumer
import com.vulpeszerda.mvvmredux.sample.BaseStateView
import com.vulpeszerda.mvvmredux.sample.GlobalState
import kotlinx.android.synthetic.main.todo_detail.message as viewMessage
import kotlinx.android.synthetic.main.todo_detail.title as viewTitle

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailStateView(
    activity: TodoDetailActivity
) :
    BaseStateView<GlobalState<TodoDetailState>>(
        "TodoDetailStateView",
        ActivityContextWrapper(activity)
    ) {

    init {
        stateConsumers.add(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev?.subState?.todo != curr?.subState?.todo },
                apply = { _, curr ->
                    viewTitle.text = curr?.subState?.todo?.title
                    viewMessage.text = curr?.subState?.todo?.message
                })
        )
        stateConsumers.add(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev?.subState?.loading != curr?.subState?.loading },
                apply = { _, curr ->
                    if (curr?.subState?.loading == true) {
                        showProgressDialog("Loading..")
                    } else {
                        hideProgressDialog()
                    }
                })
        )
    }
}