package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent

open class BaseExtraHandler(
    tag: String = "BaseExtraHandler",
    contextService: ContextService
) : AbsReduxExtraHandler(tag, contextService) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        // do nothing
    }
}
