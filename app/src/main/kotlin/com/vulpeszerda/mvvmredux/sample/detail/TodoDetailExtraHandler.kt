package com.vulpeszerda.mvvmredux.sample.detail

import android.widget.Toast
import com.vulpeszerda.mvvmredux.library.AbsExtraHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailExtraHandler(
        private val activity: TodoDetailActivity,
        errorHandler: (Throwable) -> Unit) :
        AbsExtraHandler<TodoDetailUiEvent>(activity, errorHandler) {

    override fun onExtraSideEffect(extra: SideEffect.Extra) {
        if (extra is TodoDetailSideEffect.ShowCheckedToast) {
            Toast.makeText(activity,
                    if (extra.checked) "Checked!" else "Unchecked!",
                    Toast.LENGTH_SHORT).show()
        }
    }

}