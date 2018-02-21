package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.AbsReduxErrorHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoDetailErrorHandler(activity: TodoDetailActivity) :
    AbsReduxErrorHandler("TodoDetailErrorHandler", activity) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }

}