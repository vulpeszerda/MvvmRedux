package com.github.vulpeszerda.mvvmreduxsample

interface ReduxEvent {
    interface State : ReduxEvent
    interface Extra : ReduxEvent
}