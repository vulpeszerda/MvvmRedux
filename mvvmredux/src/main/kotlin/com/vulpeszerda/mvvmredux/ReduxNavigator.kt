package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 9. 5..
 */
interface ReduxNavigator : ReduxEventPublisher {
    fun navigate(navigation: ReduxEvent.Navigation)
    fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable
}