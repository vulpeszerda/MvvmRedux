package com.github.vulpeszerda.mvvmreduxsample.detail

import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmreduxsample.model.Todo

sealed class TodoDetailEvent : ReduxEvent {
    data class Refresh(val todoUid: Long, val silent: Boolean) : TodoDetailEvent()
    data class CheckTodo(val todoUid: Long, val checked: Boolean) : TodoDetailEvent()
    data class SetLoading(val loading: Boolean) : TodoDetailEvent(), ReduxEvent.State
    data class SetTodo(val todo: Todo) : TodoDetailEvent(), ReduxEvent.State
    data class SetError(val error: Throwable?) : TodoDetailEvent(), ReduxEvent.State
    data class ShowCheckedToast(val checked: Boolean) : TodoDetailEvent(), ReduxEvent.Extra
}