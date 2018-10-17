package com.github.vulpeszerda.mvvmreduxsample

import com.github.vulpeszerda.mvvmredux.ReduxViewModel

open class BaseViewModel<T>(tag: String, initialState: GlobalState<T>) :
    ReduxViewModel<GlobalState<T>>(tag, initialState) {

    fun getSubState(block: (T) -> Unit) {
        getState {
            block(it.subState)
        }
    }

    fun setSubState(block: T.() -> T) {
        setState {
            val prevSubState = subState
            val currSubState = block(prevSubState)
            if (prevSubState !== currSubState) {
                copy(subState = currSubState)
            } else {
                this
            }
        }
    }
}