package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.ReduxEvent

sealed class TodoCreateEvent : ReduxEvent {
    data class Save(val title: String, val message: String) : TodoCreateEvent()
    data class SetLoading(val loading: Boolean) : TodoCreateEvent(), ReduxEvent.State
    object ShowFinishToast : TodoCreateEvent(), ReduxEvent.Extra
}