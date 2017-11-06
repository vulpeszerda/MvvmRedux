package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxStateView<T, E : ReduxEvent>(
        owner: LifecycleOwner,
        errorHandler: (Throwable) -> Unit) :
        AbsStateView<T, E>(owner, errorHandler) {

    abstract val isAvailable: Boolean
}