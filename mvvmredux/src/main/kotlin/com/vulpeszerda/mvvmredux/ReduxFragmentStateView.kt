package com.vulpeszerda.mvvmredux

import android.app.ProgressDialog
import android.view.View

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxFragmentStateView<T, E : ReduxEvent>(
        private val fragment: ReduxFragment,
        errorHandler: (Throwable) -> Unit) :
        ReduxStateView<T, E>(fragment, errorHandler) {

    override val progressDialog: ProgressDialog by lazy {
        ProgressDialog(fragment.context)
    }

    override val isAvailable: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false


    override val containerView: View? by lazy {
        fragment.view
    }

}