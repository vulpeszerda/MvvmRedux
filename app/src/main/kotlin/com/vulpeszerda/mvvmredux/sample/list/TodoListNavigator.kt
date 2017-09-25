package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.AbsNavigator
import com.vulpeszerda.mvvmredux.library.RouterFactory
import com.vulpeszerda.mvvmredux.library.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.TodoRouter

/**
 * Created by vulpes on 2017. 9. 21..
 */

class TodoListNavigator(activity: TodoListActivity, errorHandler: (Throwable) -> Unit) :
        AbsNavigator<TodoListEvent>(activity, errorHandler) {

    private val router: TodoRouter = RouterFactory.create(activity, TodoRouter::class.java)

    override fun navigate(navigation: ReduxEvent.Navigation) {
        when (navigation) {
            is TodoListEvent.NavigateDetail ->
                router.detail(navigation.uid)
            is TodoListEvent.NavigateCreate ->
                router.create()
        }
    }
}
