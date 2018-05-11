package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 9. 5..
 */
interface ReduxExtraHandler : ReduxComponent {
    fun onExtraEvent(extra: ReduxEvent.Extra)
    fun subscribe(source: Observable<ReduxEvent.Extra>): Disposable
}