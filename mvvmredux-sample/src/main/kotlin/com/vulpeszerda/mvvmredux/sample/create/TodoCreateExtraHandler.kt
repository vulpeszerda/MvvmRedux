package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

class TodoCreateExtraHandler(
    contextService: ContextService
) : BaseExtraHandler("TodoCreateExtraHandler", contextService) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        super.onExtraEvent(extra)
        if (extra is TodoCreateEvent.ShowFinishToast) {
            toast("Todo created")
        }
    }

}