package com.vulpeszerda.mvvmredux

import io.reactivex.Observable

interface ReduxViewModel<T> {

    val error: Observable<ReduxEvent.Error>
    val extra: Observable<ReduxEvent.Extra>
    val navigation: Observable<ReduxEvent.Navigation>
    val state: Observable<T>

    val stateStore: ReduxStore<T>?

    fun initialize(initialState: T, events: Observable<ReduxEvent>)
}