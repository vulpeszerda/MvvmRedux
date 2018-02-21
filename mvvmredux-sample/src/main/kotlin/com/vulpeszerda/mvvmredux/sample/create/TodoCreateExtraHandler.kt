package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateExtraHandler(
    activity: TodoCreateActivity
) : AbsReduxExtraHandler("TodoCreateExtraHandler", activity) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        if (extra is TodoCreateEvent.ShowFinishToast) {
            toast("Todo created")
        }
    }

}