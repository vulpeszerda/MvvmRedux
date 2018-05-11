package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper

open class ReduxComponent(
    contextService: ContextService
) : ContextService by contextService,
    ReduxEventPublisher by ReduxEventPublisher.Impl() {

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
        }
    }
}