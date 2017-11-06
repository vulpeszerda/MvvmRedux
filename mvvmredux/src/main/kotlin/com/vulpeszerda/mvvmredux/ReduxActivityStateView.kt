package com.vulpeszerda.mvvmredux

import android.view.View

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxActivityStateView<T, E : ReduxEvent>(
        private val activity: ReduxActivity,
        errorHandler: (Throwable) -> Unit) :
        ReduxStateView<T, E>(activity, errorHandler) {

    override val isAvailable: Boolean
        get() = !activity.isFinishing

    override val containerView: View? by lazy {
        activity.findViewById(android.R.id.content)
    }
}