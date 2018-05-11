package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 8. 25..
 */
interface ReduxErrorHandler : ReduxComponent {
    fun onError(error: ReduxEvent.Error)
    fun subscribe(source: Observable<ReduxEvent.Error>): Disposable
}