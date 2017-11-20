package com.vulpeszerda.mvvmredux

import io.reactivex.Observable

/**
 * Created by vulpes on 2017. 8. 25..
 */
interface ErrorHandler {
    val events: Observable<ReduxEvent>
    fun onError(error: ReduxEvent.Error)
    fun subscribe(source: Observable<ReduxEvent.Error>)
}