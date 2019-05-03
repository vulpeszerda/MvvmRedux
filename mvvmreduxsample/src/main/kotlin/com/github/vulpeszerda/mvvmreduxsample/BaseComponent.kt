package com.github.vulpeszerda.mvvmreduxsample

import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.ReduxExtraHandler
import com.github.vulpeszerda.mvvmredux.ReduxStateView
import com.github.vulpeszerda.mvvmredux.ReduxViewModel

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
