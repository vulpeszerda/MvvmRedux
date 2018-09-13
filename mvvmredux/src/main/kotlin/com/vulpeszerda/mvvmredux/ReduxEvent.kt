package com.vulpeszerda.mvvmredux

interface ReduxEvent {
    interface State : ReduxEvent
    interface Extra : ReduxEvent
}