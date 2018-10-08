package com.github.vulpeszerda.mvvmreduxsample

sealed class GlobalEvent : ReduxEvent {
    data class NavigateList(val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Extra

    data class NavigateDetail(val uid: Long, val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Extra

    data class NavigateCreate(val requestCode: Int? = null) :
        GlobalEvent(), ReduxEvent.Extra

    class NavigateFinish : GlobalEvent(), ReduxEvent.Extra

    class Error(val throwable: Throwable, tag: String?) : GlobalEvent(), ReduxEvent.Extra
}