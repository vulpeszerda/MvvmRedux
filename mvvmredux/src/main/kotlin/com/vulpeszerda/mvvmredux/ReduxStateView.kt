package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxStateView<T>(
        tag: String,
        owner: LifecycleOwner) :
        AbsStateView<T>(tag, owner) {

    abstract val isAvailable: Boolean
}