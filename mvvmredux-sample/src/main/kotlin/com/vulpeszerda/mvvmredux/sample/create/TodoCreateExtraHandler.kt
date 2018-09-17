package com.vulpeszerda.mvvmredux.sample.create

import android.widget.Toast
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

class TodoCreateExtraHandler(
    contextDelegate: ContextDelegate
) : BaseExtraHandler("TodoCreateExtraHandler", contextDelegate) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        super.onExtraEvent(extra)
        if (extra is TodoCreateEvent.ShowFinishToast) {
            Toast.makeText(getContextOrThrow(), "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

}