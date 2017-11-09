package com.vulpeszerda.mvvmredux.sample.create

import android.widget.Toast
import com.vulpeszerda.mvvmredux.AbsExtraHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateExtraHandler(
        private val activity: TodoCreateActivity) :
        AbsExtraHandler("TodoCreateExtraHandler", activity) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        if (extra is TodoCreateEvent.ShowFinishToast) {
            Toast.makeText(activity, "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

}