package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 8. 31..
 */
sealed class TodoCreateSideEffect {
    data class SetLoading(val loading: Boolean) : TodoCreateSideEffect(), SideEffect.State
    class NavigateFinish : TodoCreateSideEffect(), SideEffect.Navigation
    class ShowFinishToast : TodoCreateSideEffect(), SideEffect.Extra
}