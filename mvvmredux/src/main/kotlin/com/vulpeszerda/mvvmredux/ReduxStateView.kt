package com.vulpeszerda.mvvmredux

import android.app.ProgressDialog
import android.arch.lifecycle.LifecycleOwner
import android.support.annotation.UiThread

/**
 * Created by vulpes on 2017. 9. 22..
 */
abstract class ReduxStateView<T, E : ReduxEvent>(
        owner: LifecycleOwner,
        errorHandler: (Throwable) -> Unit) :
        AbsStateView<T, E>(owner, errorHandler) {

    abstract val progressDialog: ProgressDialog
    abstract val isAvailable: Boolean

    @UiThread
    open fun showProgressDialog() {
        showProgressDialog(null)
    }

    @UiThread
    open fun showProgressDialog(message: String?) {
        showProgressDialog(null, message)
    }

    @UiThread
    open fun showProgressDialog(title: String?, message: String?) {
        if (isAvailable) {
            with(progressDialog) {
                setTitle(title)
                setMessage(message)
                setCancelable(false)
                if (!isShowing) {
                    show()
                }
            }
        }
    }

    @UiThread
    open fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}