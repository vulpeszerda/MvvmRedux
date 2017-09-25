package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 8. 30..
 */
sealed class TodoListEvent : ReduxEvent {
    class ConfirmClearAll : TodoListEvent()
    data class Refresh(val silent: Boolean) : TodoListEvent()

    data class SetLoading(val loading: Boolean) : TodoListEvent(), ReduxEvent.State
    data class SetTodos(val todos: List<Todo>) : TodoListEvent(), ReduxEvent.State
    data class SetError(val error: Throwable?) : TodoListEvent(), ReduxEvent.State
    data class CheckTodo(val uid: Long, val checked: Boolean) : TodoListEvent(), ReduxEvent.State

    class NavigateCreate : TodoListEvent(), ReduxEvent.Navigation
    data class NavigateDetail(val uid: Long) : TodoListEvent(), ReduxEvent.Navigation
    class ShowClearConfirm : TodoListEvent(), ReduxEvent.Extra
    class ShowClearedToast : TodoListEvent(), ReduxEvent.Extra
}