package com.vulpeszerda.mvvmredux

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface ReduxBinder<T> {

    fun bind(initialState: T): Disposable

    open class Impl<T>(private val reduxContext: ReduxContext<T>) : ReduxBinder<T> {

        override fun bind(initialState: T): Disposable {
            initViewModel(initialState)
            return bindComponent(reduxContext)
        }

        protected open fun initViewModel(initialState: T) {
            reduxContext.viewModel.initialize(initialState, reduxContext.eventStream)
        }

        protected open fun bindComponent(component: ReduxContext<T>): Disposable =
            component.viewModel.let {
                CompositeDisposable(
                    component.navigator.subscribe(it.navigation),
                    component.extraHandler.subscribe(it.extra),
                    component.errorHandler.subscribe(it.error),
                    component.stateView.subscribe(it.state)
                )
            }
    }
}