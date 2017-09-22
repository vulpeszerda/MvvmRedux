package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.AbsNavigator
import com.vulpeszerda.mvvmredux.library.RouterFactory
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.TodoRouter

/**
 * Created by vulpes on 2017. 9. 21..
 */

class TodoListNavigator(activity: TodoListActivity, errorHandler: (Throwable) -> Unit) :
        AbsNavigator<TodoListUiEvent>(activity, errorHandler) {

    private val router: TodoRouter = RouterFactory.create(activity, TodoRouter::class.java)

    override fun navigate(navigation: SideEffect.Navigation) {
        when (navigation) {
            is TodoListSideEffect.NavigateDetail ->
                router.detail(navigation.uid)
            is TodoListSideEffect.NavigateCreate ->
                router.create()
        }
    }
}
