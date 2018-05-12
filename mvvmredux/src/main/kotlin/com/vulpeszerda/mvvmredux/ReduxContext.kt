package com.vulpeszerda.mvvmredux

interface ReduxContext<T> {

    val viewModel: ReduxViewModel<T>

    val extraHandler: ReduxExtraHandler

    val stateView: ReduxStateView<T>

    val reduxComponents: List<ReduxComponent>

    abstract class AbsImpl<T> : ReduxContext<T> {

        override val reduxComponents: List<ReduxComponent> by lazy {
            listOf(extraHandler, stateView)
        }
    }
}