package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 9. 21..
 */
@Suppress("unused")
abstract class AbsReduxNavigator(
    protected val tag: String,
    contextService: ContextService
) : ReduxComponent(contextService),
    ReduxNavigator {

    override fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable =
        source.bufferUntilOnResumed(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { available }
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe(this::navigate) {
                ReduxFramework.onFatalError(it, tag)
            }
}