package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 8. 31..
 */
sealed class TodoListSideEffect {
    data class SetLoading(val loading: Boolean) : TodoListSideEffect(), SideEffect.State
    data class SetTodos(val todos: List<Todo>) : TodoListSideEffect(), SideEffect.State
    data class SetError(val error: Throwable?) : TodoListSideEffect(), SideEffect.State
    data class CheckTodo(val uid: Long,
                         val checked: Boolean) : TodoListSideEffect(), SideEffect.State

    class NavigateCreate : TodoListSideEffect(), SideEffect.Navigation
    data class NavigateDetail(val uid: Long) : TodoListSideEffect(), SideEffect.Navigation
    class ShowClearConfirm : TodoListSideEffect(), SideEffect.Extra
    class ShowClearedToast : TodoListSideEffect(), SideEffect.Extra
}