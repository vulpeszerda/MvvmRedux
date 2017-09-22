package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.AbsErrorHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateErrorHandler(private val activity: TodoCreateActivity) :
        AbsErrorHandler<TodoCreateUiEvent>(activity) {

    override fun onError(error: SideEffect.Error) {
        error.throwable.printStackTrace()
    }

}