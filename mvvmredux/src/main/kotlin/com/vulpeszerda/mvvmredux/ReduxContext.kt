package com.vulpeszerda.mvvmredux

import io.reactivex.Observable

interface ReduxContext<T> {

    val viewModel: ReduxViewModel<T>

    val navigator: ReduxNavigator

    val extraHandler: ReduxExtraHandler

    val errorHandler: ReduxErrorHandler

    val stateView: ReduxStateView<T>

    val eventStream: Observable<ReduxEvent>

    abstract class AbsImpl<T> : ReduxContext<T> {

        override val eventStream: Observable<ReduxEvent> =
            Observable.mergeArray(
                navigator.events,
                extraHandler.events,
                errorHandler.events,
                stateView.events
            )

    }
}