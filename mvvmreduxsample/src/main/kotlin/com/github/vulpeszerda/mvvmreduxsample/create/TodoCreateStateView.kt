package com.github.vulpeszerda.mvvmreduxsample.create

import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmreduxsample.BaseStateView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_create.*
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

class TodoCreateStateView(
    contextDelegate: com.github.vulpeszerda.mvvmredux.ContextDelegate
) : BaseStateView<TodoCreateState>("TodoCreateStateView", contextDelegate) {

    override val events: Observable<ReduxEvent>
        get() = super.events.mergeWith(RxView.clicks(btn_save)
            .map { Pair(viewTitle.text?.toString(), viewMessage.text?.toString()) }
            .filter { it.first != null && it.second != null }
            .map<TodoCreateEvent> { (title, message) ->
                TodoCreateEvent.Save(
                    requireNotNull(title),
                    requireNotNull(message)
                )
            })

    init {
        addConsumer({ it.subState.loading }) { _, curr ->
            if (curr.subState.loading) {
                showProgressDialog("Loading")
            } else {
                hideProgressDialog()
            }
        }
    }
}