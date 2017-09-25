package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.AbsErrorHandler
import com.vulpeszerda.mvvmredux.library.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateErrorHandler(private val activity: TodoCreateActivity) :
        AbsErrorHandler<TodoCreateEvent>(activity) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }

}