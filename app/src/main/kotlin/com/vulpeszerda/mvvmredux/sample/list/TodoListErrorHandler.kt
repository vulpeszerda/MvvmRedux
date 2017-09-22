package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.AbsErrorHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 21..
 */

class TodoListErrorHandler(activity: TodoListActivity) :
        AbsErrorHandler<TodoListUiEvent>(activity) {

    override fun onError(error: SideEffect.Error) {
        error.throwable.printStackTrace()
    }
}

