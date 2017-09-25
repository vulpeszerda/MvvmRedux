package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 9. 22..
 */
sealed class TodoDetailEvent : ReduxEvent {
    data class Refresh(val todoUid: Long, val silent: Boolean) : TodoDetailEvent()
    data class CheckTodo(val todoUid: Long, val checked: Boolean) : TodoDetailEvent()
    data class SetLoading(val loading: Boolean) : TodoDetailEvent(), ReduxEvent.State
    data class SetTodo(val todo: Todo) : TodoDetailEvent(), ReduxEvent.State
    data class SetError(val error: Throwable?) : TodoDetailEvent(), ReduxEvent.State
    class ShowCheckedToast(val checked: Boolean) : TodoDetailEvent(), ReduxEvent.Extra
}