package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.AbsNavigator
import com.vulpeszerda.mvvmredux.RouterFactory
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.TodoRouter

/**
 * Created by vulpes on 2017. 9. 21..
 */

class TodoListNavigator(activity: TodoListActivity) :
        AbsNavigator("TodoListNavigator", activity) {

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
