package com.vulpeszerda.mvvmredux.library

import io.reactivex.Observable

/**
 * Created by vulpes on 2017. 9. 5..
 */
interface ExtraHandler {
    fun onExtraSideEffect(extra: SideEffect.Extra)
    fun subscribe(source: Observable<SideEffect.Extra>)
}