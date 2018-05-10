package com.vulpeszerda.mvvmredux

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface ReduxBinder {

    fun setupViewModel(
        savedInstanceState: Bundle?,
        vararg extraStream: Observable<ReduxEvent>
    ): Disposable

    abstract class AbsImpl<T>(private val component: Component<T>) : ReduxBinder {

        override fun setupViewModel(
            savedInstanceState: Bundle?,
            vararg extraStream: Observable<ReduxEvent>
        ): Disposable {
            val initialState = restoreStateFromBundle(savedInstanceState)
            val stream = getEventStream(component, Observable.mergeArray(*extraStream))
            initializeViewModel(component, initialState, stream)
            return bindComponent(component)
        }

        protected open fun initializeViewModel(
            component: Component<T>,
            initialState: T,
            stream: Observable<ReduxEvent>
        ) {
            component.viewModel.initialize(initialState, stream)
        }

        protected open fun bindComponent(component: Component<T>): Disposable =
            component.viewModel.let {
                CompositeDisposable(
                    component.navigator.subscribe(it.navigation),
                    component.extraHandler.subscribe(it.extra),
                    component.errorHandler.subscribe(it.error),
                    component.stateView.subscribe(it.state)
                )
            }

        protected open fun getEventStream(
            component: Component<T>,
            extraStream: Observable<ReduxEvent>
        ): Observable<ReduxEvent> =
            Observable.empty<ReduxEvent>()
                .mergeWith(extraStream)
                .mergeWith(component.stateView.events)
                .mergeWith(component.navigator.events)
                .mergeWith(component.extraHandler.events)
                .mergeWith(component.errorHandler.events)

        protected abstract fun restoreStateFromBundle(savedInstanceState: Bundle?): T
    }

    class SimpleImpl<T>(
        component: Component<T>,
        private val restoreStateDelegate: (Bundle?) -> T
    ) : AbsImpl<T>(component) {

        override fun restoreStateFromBundle(savedInstanceState: Bundle?): T =
            restoreStateDelegate(savedInstanceState)
    }
}