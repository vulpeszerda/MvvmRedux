package com.github.vulpeszerda.mvvmredux

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

interface ReduxComponent : ReduxEventPublisher, LifecycleObserver {

    fun bindToLifecycle()

    open class Impl(
        contextDelegate: com.github.vulpeszerda.mvvmredux.ContextDelegate
    ) : ReduxComponent,
        com.github.vulpeszerda.mvvmredux.ContextDelegate by contextDelegate,
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
