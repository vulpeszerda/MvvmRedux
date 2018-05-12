package com.vulpeszerda.mvvmredux

/**
 * Created by vulpes on 2017. 8. 29..
 */
interface ReduxEvent {
    interface State : ReduxEvent
    interface Extra : ReduxEvent
}