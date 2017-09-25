package com.vulpeszerda.mvvmredux.library

import io.reactivex.Observable

/**
 * Created by vulpes on 2017. 8. 25..
 */
interface ErrorHandler {
    fun onError(error: ReduxEvent.Error)
    fun subscribe(source: Observable<ReduxEvent.Error>)
}