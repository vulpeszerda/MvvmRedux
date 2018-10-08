package com.github.vulpeszerda.mvvmreduxsample.create

import android.widget.Toast
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler
import com.github.vulpeszerda.mvvmreduxsample.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.ReduxEvent

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