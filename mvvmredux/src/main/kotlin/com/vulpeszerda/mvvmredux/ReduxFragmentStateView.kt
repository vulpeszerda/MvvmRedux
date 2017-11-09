package com.vulpeszerda.mvvmredux

import android.app.ProgressDialog
import android.view.View

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxFragmentStateView<T>(
        tag: String,
        private val fragment: ReduxFragment) :
        ReduxStateView<T>(tag, fragment) {

    override val isAvailable: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false


    override val containerView: View? by lazy {
        fragment.view
    }

}