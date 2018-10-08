package com.github.vulpeszerda.mvvmreduxsample

import io.reactivex.Observable

interface ReduxViewModel<T> {

    val extra: Observable<ReduxEvent.Extra>
    val state: Observable<T>

    val stateStore: ReduxStore<T>?

    fun initialize(initialState: T, events: Observable<ReduxEvent>)
}