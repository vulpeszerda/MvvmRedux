package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.AbsNavigator
import com.vulpeszerda.mvvmredux.RouterFactory
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.TodoRouter

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateNavigator(activity: TodoCreateActivity) :
        AbsNavigator("TodoCreateNavigator", activity) {

    private val router = RouterFactory.create(activity, TodoRouter::class.java)

    override fun navigate(navigation: ReduxEvent.Navigation) {
        if (navigation is TodoCreateEvent.NavigateFinish) {
            router.finish()
        }
    }
}