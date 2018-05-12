package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailExtraHandler(
    contextService: ContextService
) : BaseExtraHandler("TodoDetailExtraHandler", contextService) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        super.onExtraEvent(extra)
        if (extra is TodoDetailEvent.ShowCheckedToast) {
            toast(if (extra.checked) "Checked!" else "Unchecked!")
        }
    }

}