package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.view.View

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class FragmentContextWrapper(protected val fragment: ReduxFragment) : ContextWrapper {

    override val owner: LifecycleOwner
        get() = fragment

    override val containerView: View?
        get() = fragment.view

    override val available: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false

    override val context: Context
        get() = fragment.context
}