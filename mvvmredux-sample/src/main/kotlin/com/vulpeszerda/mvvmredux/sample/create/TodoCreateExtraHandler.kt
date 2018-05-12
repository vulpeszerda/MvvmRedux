package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

/**
 * Created by vulpes on 2017. 9. 21..
 */
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