package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailExtraHandler(
    contextService: ContextService
) : AbsReduxExtraHandler("TodoDetailExtraHandler", contextService) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        if (extra is TodoDetailEvent.ShowCheckedToast) {
            toast(if (extra.checked) "Checked!" else "Unchecked!")
        }
    }

}