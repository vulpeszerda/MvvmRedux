package com.github.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface ReduxStateView<T> : ReduxComponent {
    fun subscribe(source: Observable<T>): Disposable
}
