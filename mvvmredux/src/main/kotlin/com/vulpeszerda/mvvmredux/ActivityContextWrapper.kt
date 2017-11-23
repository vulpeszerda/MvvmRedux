package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.view.View

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class ActivityContextWrapper(protected val activity: ReduxActivity) : ContextWrapper {

    override val owner: LifecycleOwner
        get() = activity

    override val containerView: View? by lazy {
        activity.findViewById(android.R.id.content)
    }

    override val available: Boolean
        get() = !activity.isFinishing

    override val context: Context
        get() = activity

}