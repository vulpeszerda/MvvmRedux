package com.vulpeszerda.mvvmredux

import android.view.View

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxActivityStateView<T>(
        tag: String,
        protected val activity: ReduxActivity) :
        ReduxStateView<T>(tag, activity) {

    override val isAvailable: Boolean
        get() = !activity.isFinishing

    override val containerView: View? by lazy {
        activity.findViewById(android.R.id.content)
    }
}