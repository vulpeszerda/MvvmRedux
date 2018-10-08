package com.github.vulpeszerda.mvvmredux.sample.create

import com.github.vulpeszerda.mvvmredux.ReduxEvent

sealed class TodoCreateEvent : ReduxEvent {
    data class Save(val title: String, val message: String) : TodoCreateEvent()
    data class SetLoading(val loading: Boolean) : TodoCreateEvent(), ReduxEvent.State
    object ShowFinishToast : TodoCreateEvent(), ReduxEvent.Extra
}