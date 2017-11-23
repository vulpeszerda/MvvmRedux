package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.ActivityContextWrapper
import com.vulpeszerda.mvvmredux.sample.BaseStateView
import com.vulpeszerda.mvvmredux.sample.GlobalState
import kotlinx.android.synthetic.main.todo_detail.message as viewMessage
import kotlinx.android.synthetic.main.todo_detail.title as viewTitle

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailStateView(
        activity: TodoDetailActivity) :
        BaseStateView<GlobalState<TodoDetailState>>(
                "TodoDetailStateView",
                ActivityContextWrapper(activity)) {

    override fun onStateChanged(prev: GlobalState<TodoDetailState>?,
                                curr: GlobalState<TodoDetailState>?) {

        if (prev?.subState?.todo != curr?.subState?.todo) {
            viewTitle.text = curr?.subState?.todo?.title
            viewMessage.text = curr?.subState?.todo?.message
        }

        if (prev?.subState?.loading != curr?.subState?.loading) {
            if (curr?.subState?.loading == true) {
                showProgressDialog("Loading..")
            } else {
                hideProgressDialog()
            }
        }
    }
}