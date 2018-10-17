package com.github.vulpeszerda.mvvmreduxsample.detail

import android.widget.Toast
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler

class TodoDetailExtraHandler(
    contextDelegate: ContextDelegate
) : BaseExtraHandler("TodoDetailExtraHandler", contextDelegate) {

    fun showCheckedToast(checked: Boolean) {
        ensureResumed {
            Toast.makeText(
                getContextOrThrow(),
                if (checked) "Checked!" else "Unchecked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}