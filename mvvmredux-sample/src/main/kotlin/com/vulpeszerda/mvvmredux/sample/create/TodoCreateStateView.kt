package com.vulpeszerda.mvvmredux.sample.create

import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.StateConsumer
import com.vulpeszerda.mvvmredux.sample.BaseStateView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_create.*
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

class TodoCreateStateView(
    contextService: ContextService
) : BaseStateView<TodoCreateState>("TodoCreateStateView", contextService) {

    override val events: Observable<ReduxEvent>
        get() = super.events.mergeWith(RxView.clicks(btn_save)
            .map { Pair(viewTitle.text?.toString(), viewMessage.text?.toString()) }
            .filter { it.first != null && it.second != null }
            .map<TodoCreateEvent> { (title, message) ->
                TodoCreateEvent.Save(title!!, message!!)
            })

    init {
        addConsumer(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev.subState.loading != curr.subState.loading },
                apply = { _, curr ->
                    if (curr.subState.loading) {
                        showProgressDialog("Loading")
                    } else {
                        hideProgressDialog()
                    }
                })
        )
    }
}