package com.github.vulpeszerda.mvvmredux

interface ReduxEvent {
    interface State : ReduxEvent
    interface Extra : ReduxEvent
}