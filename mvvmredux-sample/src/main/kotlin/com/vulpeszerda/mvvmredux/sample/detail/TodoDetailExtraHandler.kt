package com.vulpeszerda.mvvmredux.sample.detail

import android.widget.Toast
import com.vulpeszerda.mvvmredux.AbsExtraHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailExtraHandler(
        private val activity: TodoDetailActivity,
        errorHandler: (Throwable) -> Unit) :
        AbsExtraHandler<TodoDetailEvent>(activity, errorHandler) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        if (extra is TodoDetailEvent.ShowCheckedToast) {
            Toast.makeText(activity,
                    if (extra.checked) "Checked!" else "Unchecked!",
                    Toast.LENGTH_SHORT).show()
        }
    }

}