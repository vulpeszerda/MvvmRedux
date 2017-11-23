package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 8. 31..
 */
sealed class TodoCreateEvent: ReduxEvent {
    data class Save(val title: String, val message: String) : TodoCreateEvent()
    data class SetLoading(val loading: Boolean) : TodoCreateEvent(), ReduxEvent.State
    class ShowFinishToast : TodoCreateEvent(), ReduxEvent.Extra
}