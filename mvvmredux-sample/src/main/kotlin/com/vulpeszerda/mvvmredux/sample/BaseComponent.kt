package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.*

open class BaseComponent<T>(
    protected val contextDelegate: ContextDelegate
) : ReduxContext.AbsImpl<GlobalState<T>>() {

    override val viewModel: ReduxViewModel<GlobalState<T>> by lazy {
        throw NotImplementedError()
    }

    override val extraHandler: ReduxExtraHandler by lazy {
        BaseExtraHandler(contextDelegate = contextDelegate)
    }

    override val stateView: ReduxStateView<GlobalState<T>> by lazy {
        BaseStateView<T>(contextDelegate = contextDelegate)
    }

}
