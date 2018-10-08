package com.github.vulpeszerda.mvvmredux.sample.create

import android.widget.Toast
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmredux.sample.BaseExtraHandler

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