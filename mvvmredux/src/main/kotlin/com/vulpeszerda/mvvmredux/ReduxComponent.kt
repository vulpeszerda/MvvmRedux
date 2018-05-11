package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper

interface ReduxComponent : ReduxEventPublisher, LifecycleObserver {

    fun bindToLifecycle()

    open class Impl(
        contextService: ContextService
    ) : ReduxComponent,
        ContextService by contextService,
        ReduxEventPublisher by ReduxEventPublisher.Impl() {

        override fun bindToLifecycle() {
            owner.lifecycle.addObserver(this)
        }

        @CallSuper
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        open fun onLifecycleEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                owner.lifecycle.removeObserver(this)
            }
        }
    }
}
