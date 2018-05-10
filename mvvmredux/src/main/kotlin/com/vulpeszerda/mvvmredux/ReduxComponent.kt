package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper
import io.reactivex.android.schedulers.AndroidSchedulers

open class ReduxComponent(
    contextService: ContextService
) : ContextService by contextService,
    ReduxEventPublisher by ReduxEventPublisher.Impl() {

    init {
        AndroidSchedulers.mainThread().createWorker().schedule {
            @Suppress("LeakingThis")
            contextService.owner.lifecycle.addObserver(this)
        }
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
        }
    }
}