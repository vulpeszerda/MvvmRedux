package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 11. 23..
 */
sealed class GlobalEvent : ReduxEvent {
    data class NavigateList(val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Navigation

    data class NavigateDetail(val uid: Long, val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Navigation

    data class NavigateCreate(val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Navigation

    class NavigateFinish : GlobalEvent(), ReduxEvent.Navigation
}