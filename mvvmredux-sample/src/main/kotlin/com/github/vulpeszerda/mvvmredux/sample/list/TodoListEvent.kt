package com.github.vulpeszerda.mvvmredux.sample.list

import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmredux.sample.model.Todo

sealed class TodoListEvent : ReduxEvent {
    object ConfirmClearAll : TodoListEvent()
    data class Refresh(val silent: Boolean) : TodoListEvent()

    data class SetLoading(val loading: Boolean) : TodoListEvent(), ReduxEvent.State
    data class SetTodos(val todos: List<Todo>) : TodoListEvent(), ReduxEvent.State
    data class SetError(val error: Throwable?) : TodoListEvent(), ReduxEvent.State
    data class CheckTodo(val uid: Long, val checked: Boolean) : TodoListEvent(), ReduxEvent.State

    class ShowClearConfirm : TodoListEvent(), ReduxEvent.Extra
    class ShowClearedToast : TodoListEvent(), ReduxEvent.Extra
}