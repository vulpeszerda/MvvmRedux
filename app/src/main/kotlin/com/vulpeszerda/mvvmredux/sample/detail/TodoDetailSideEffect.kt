package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 9. 22..
 */
sealed class TodoDetailSideEffect {
    data class SetLoading(val loading: Boolean) : TodoDetailSideEffect(), SideEffect.State
    data class SetTodo(val todo: Todo) : TodoDetailSideEffect(), SideEffect.State
    data class SetError(val error: Throwable?) : TodoDetailSideEffect(), SideEffect.State
    class ShowCheckedToast(val checked: Boolean) : TodoDetailSideEffect(), SideEffect.Extra
}