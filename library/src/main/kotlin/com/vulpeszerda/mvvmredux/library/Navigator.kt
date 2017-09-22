package com.vulpeszerda.mvvmredux.library

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 9. 5..
 */
interface Navigator {
    fun navigate(navigation: SideEffect.Navigation)
    fun subscribe(source: Observable<SideEffect.Navigation>): Disposable
}