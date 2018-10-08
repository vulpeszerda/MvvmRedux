package com.github.vulpeszerda.mvvmreduxsample.detail

import android.widget.Toast
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler
import com.github.vulpeszerda.mvvmreduxsample.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.ReduxEvent

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