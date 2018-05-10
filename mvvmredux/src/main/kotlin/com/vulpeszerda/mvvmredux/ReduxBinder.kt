package com.vulpeszerda.mvvmredux

import android.os.Bundle
import io.reactivex.Observable

interface ReduxBinder {

    fun setupViewModel(
        savedInstanceState: Bundle?,
        extraStream: Observable<ReduxEvent> = Observable.empty()
    )

    abstract class AbsImpl<T>(private val component: Component<T>) : ReduxBinder {

        override fun setupViewModel(
            savedInstanceState: Bundle?,
            extraStream: Observable<ReduxEvent>
        ) {
            val initialState = restoreStateFromBundle(savedInstanceState)
            val stream = getEventStream(component, extraStream)
            initializeViewModel(component, initialState, stream)
            bindComponent(component)
        }

        protected open fun initializeViewModel(
            component: Component<T>,
            initialState: T,
            stream: Observable<ReduxEvent>
        ) {
            component.viewModel.initialize(initialState, stream)
        }

        protected open fun bindComponent(component: Component<T>) {
            component.viewModel.apply {
                component.navigator.subscribe(navigation)
                component.extraHandler.subscribe(extra)
                component.errorHandler.subscribe(error)
                component.stateView.subscribe(state)
            }
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