package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.AbsNavigator
import com.vulpeszerda.mvvmredux.library.RouterFactory
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.TodoRouter

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateNavigator(activity: TodoCreateActivity, errorHandler: (Throwable) -> Unit) :
        AbsNavigator<TodoCreateUiEvent>(activity, errorHandler) {

    private val router = RouterFactory.create(activity, TodoRouter::class.java)

    override fun navigate(navigation: SideEffect.Navigation) {
        if (navigation is TodoCreateSideEffect.NavigateFinish) {
            router.finish()
        }
    }
}