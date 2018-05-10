package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 9. 22..
 */
interface ReduxStateView<T> : ReduxEventPublisher {
    fun subscribe(source: Observable<T>): Disposable
}
