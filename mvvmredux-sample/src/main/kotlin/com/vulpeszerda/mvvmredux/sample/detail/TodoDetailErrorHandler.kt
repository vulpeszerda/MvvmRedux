package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.AbsErrorHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoDetailErrorHandler(activity: TodoDetailActivity) :
        AbsErrorHandler("TodoDetailErrorHandler", activity) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }

}