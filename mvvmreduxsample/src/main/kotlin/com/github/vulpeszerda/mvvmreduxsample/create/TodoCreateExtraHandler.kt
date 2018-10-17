package com.github.vulpeszerda.mvvmreduxsample.create

import android.widget.Toast
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler

class TodoCreateExtraHandler(
    contextDelegate: ContextDelegate
) : BaseExtraHandler("TodoCreateExtraHandler", contextDelegate) {

    fun showFinishToast() {
        ensureResumed {
            Toast.makeText(getContextOrThrow(), "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

}