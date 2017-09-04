package com.vulpeszerda.mvvmredux.library

import io.reactivex.Flowable

/**
 * Created by vulpes on 2017. 8. 29..
 */
interface BaseViewModelDelegate<E : UiEvent> : UiSchedulerProvider, ExceptionHandler {

    val events: Flowable<E>

    fun navigate(navigation: SideEffect.Navigation)

    fun handleExtraSideEffect(sideEffect: SideEffect.Extra)
}