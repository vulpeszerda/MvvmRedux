package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.AbsErrorHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */

class TodoListErrorHandler(activity: TodoListActivity) :
        AbsErrorHandler<TodoListEvent>(activity) {

    override fun onError(error: ReduxEvent.Error) {
        error.throwable.printStackTrace()
    }
}

