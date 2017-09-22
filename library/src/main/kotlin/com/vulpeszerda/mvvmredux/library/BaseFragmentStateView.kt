package com.vulpeszerda.mvvmredux.library

import android.app.ProgressDialog
import android.view.View

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class BaseFragmentStateView<T, E : UiEvent>(
        private val fragment: BaseFragment,
        errorHandler: (Throwable) -> Unit) :
        BaseStateView<T, E>(fragment, errorHandler) {

    override val progressDialog: ProgressDialog by lazy {
        ProgressDialog(fragment.context)
    }

    override val isAvailable: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false


    override val containerView: View? by lazy {
        fragment.view
    }

}