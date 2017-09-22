package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.AbsErrorHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoDetailErrorHandler(private val activity: TodoDetailActivity) :
        AbsErrorHandler<TodoDetailUiEvent>(activity) {

    override fun onError(error: SideEffect.Error) {
        error.throwable.printStackTrace()
    }

}