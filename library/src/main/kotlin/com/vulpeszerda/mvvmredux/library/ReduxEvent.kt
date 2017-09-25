package com.vulpeszerda.mvvmredux.library

/**
 * Created by vulpes on 2017. 8. 29..
 */
interface ReduxEvent {
    interface State : ReduxEvent
    interface Navigation : ReduxEvent
    interface Extra : ReduxEvent
    data class Error(val throwable: Throwable, val tag: String? = null) : ReduxEvent
}