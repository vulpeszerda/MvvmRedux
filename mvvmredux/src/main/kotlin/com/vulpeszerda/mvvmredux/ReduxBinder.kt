package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

object ReduxBinder {

    fun <T> bind(
        reduxContext: ReduxContext<T>,
        initialState: T,
        extraStream: Observable<ReduxEvent> = Observable.empty()
    ): Disposable {
        reduxContext.reduxComponents.forEach { it.bindToLifecycle() }
        reduxContext.viewModel.initialize(
            initialState,
            Observable.merge(reduxContext.reduxComponents.map { it.events })
                .mergeWith(extraStream)
        )

        return reduxContext.viewModel.let {
            CompositeDisposable(
                reduxContext.extraHandler.subscribe(it.extra),
                reduxContext.stateView.subscribe(it.state)
            )
        }
    }
}