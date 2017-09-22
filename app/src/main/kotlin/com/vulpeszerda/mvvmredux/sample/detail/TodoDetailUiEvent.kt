package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.UiEvent

/**
 * Created by vulpes on 2017. 9. 22..
 */
sealed class TodoDetailUiEvent : UiEvent {
    data class Refresh(val todoUid: Long, val silent: Boolean) : TodoDetailUiEvent()
    data class CheckTodo(val todoUid: Long, val checked: Boolean) : TodoDetailUiEvent()
}