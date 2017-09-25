package com.vulpeszerda.mvvmredux.sample.create

import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.library.BaseActivityStateView
import com.vulpeszerda.mvvmredux.library.GlobalState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_create.*
import kotlinx.android.synthetic.main.todo_create.message as viewMessage
import kotlinx.android.synthetic.main.todo_create.title as viewTitle

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoCreateStateView(
        private val activity: TodoCreateActivity,
        errorHandler: (Throwable) -> Unit) :
        BaseActivityStateView<TodoCreateState, TodoCreateEvent>(activity, errorHandler) {

    override val events: Observable<TodoCreateEvent>
        get() = super.events.mergeWith(RxView.clicks(btn_save)
                .map { Pair(viewTitle.text?.toString(), viewMessage.text?.toString()) }
                .filter { it.first != null && it.second != null }
                .map<TodoCreateEvent> { (title, message) ->
                    TodoCreateEvent.Save(title!!, message!!)
                })

    override fun onStateChanged(prev: GlobalState<TodoCreateState>?,
                                curr: GlobalState<TodoCreateState>?) {

        if (prev?.subState?.loading != curr?.subState?.loading) {
            if (curr?.subState?.loading == true) {
                showProgressDialog("Loading")
            } else {
                hideProgressDialog()
            }
        }
    }

}