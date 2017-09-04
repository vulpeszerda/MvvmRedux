package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.UiEvent
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 8. 30..
 */
sealed class TodoListUiEvent : UiEvent {
    class ClickClearAll : TodoListUiEvent()
    class ConfirmClearAll : TodoListUiEvent()
    data class ClickDetail(val uid: Long) : TodoListUiEvent()
    class ClickCreate : TodoListUiEvent()
    class Refresh : TodoListUiEvent()
    data class CheckTodo(val todo: Todo) : TodoListUiEvent()
}