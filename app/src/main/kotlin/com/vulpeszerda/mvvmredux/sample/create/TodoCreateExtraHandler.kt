package com.vulpeszerda.mvvmredux.sample.create

import android.widget.Toast
import com.vulpeszerda.mvvmredux.library.AbsExtraHandler
import com.vulpeszerda.mvvmredux.library.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateExtraHandler(
        private val activity: TodoCreateActivity,
        errorHandler: (Throwable) -> Unit) :
        AbsExtraHandler<TodoCreateEvent>(activity, errorHandler) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        if (extra is TodoCreateEvent.ShowFinishToast) {
            Toast.makeText(activity, "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

}