package com.vulpeszerda.mvvmredux

import io.reactivex.Observable

/**
 * Created by vulpes on 2017. 9. 5..
 */
interface ReduxExtraHandler {
    val events: Observable<ReduxEvent>
    fun onExtraEvent(extra: ReduxEvent.Extra)
    fun subscribe(source: Observable<ReduxEvent.Extra>)
}