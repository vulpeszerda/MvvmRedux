package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.AbsErrorHandler
import com.vulpeszerda.mvvmredux.library.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoDetailErrorHandler(private val activity: TodoDetailActivity) :
        AbsErrorHandler<TodoDetailEvent>(activity) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }

}