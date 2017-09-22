package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner

/**
 * Created by vulpes on 2017. 8. 25..
 */
class AbsLifecycleRegistryOwner : LifecycleRegistryOwner {
    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry
}