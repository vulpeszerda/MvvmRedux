package com.github.vulpeszerda.mvvmreduxsample

open class BaseComponent<T>(
    protected val contextDelegate: ContextDelegate
) {

    open val viewModel: ReduxViewModel<GlobalState<T>> by lazy {
        throw NotImplementedError()
    }

    open val extraHandler: ReduxExtraHandler by lazy {
        BaseExtraHandler(contextDelegate = contextDelegate)
    }

    open val stateView: ReduxStateView<GlobalState<T>> by lazy {
        BaseStateView<T>(contextDelegate = contextDelegate)
    }

}
