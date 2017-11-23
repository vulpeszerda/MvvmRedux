package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by vulpes on 2017. 11. 23..
 */
interface ContextWrapper : LayoutContainer, LifecycleObserver {
    val owner: LifecycleOwner
    val available: Boolean
    val context: Context
}