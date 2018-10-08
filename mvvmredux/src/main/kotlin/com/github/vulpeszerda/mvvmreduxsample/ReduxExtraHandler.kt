package com.github.vulpeszerda.mvvmreduxsample

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface ReduxExtraHandler : ReduxComponent {
    fun onExtraEvent(extra: ReduxEvent.Extra)
    fun subscribe(source: Observable<ReduxEvent.Extra>): Disposable
}