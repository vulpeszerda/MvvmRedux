package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.UiEvent

/**
 * Created by vulpes on 2017. 8. 31..
 */
sealed class TodoCreateUiEvent : UiEvent {
    data class Save(val title: String, val message: String) : TodoCreateUiEvent()
}