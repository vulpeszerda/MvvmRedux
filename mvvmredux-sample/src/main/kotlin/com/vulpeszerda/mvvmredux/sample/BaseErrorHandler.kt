package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.AbsReduxErrorHandler
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent

open class BaseErrorHandler(
    tag: String = "BaseExtraHandler",
    contextService: ContextService
) : AbsReduxErrorHandler(tag, contextService) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }
}
