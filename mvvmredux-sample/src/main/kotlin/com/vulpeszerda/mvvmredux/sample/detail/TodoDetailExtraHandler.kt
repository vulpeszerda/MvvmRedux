package com.vulpeszerda.mvvmredux.sample.detail

import android.widget.Toast
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

class TodoDetailExtraHandler(
    contextDelegate: ContextDelegate
) : BaseExtraHandler("TodoDetailExtraHandler", contextDelegate) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        super.onExtraEvent(extra)
        if (extra is TodoDetailEvent.ShowCheckedToast) {
            Toast.makeText(
                getContextOrThrow(),
                if (extra.checked) "Checked!" else "Unchecked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}