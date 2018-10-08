package com.github.vulpeszerda.mvvmreduxsample

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface ReduxStateView<T> : ReduxComponent {
    fun subscribe(source: Observable<T>): Disposable
}
