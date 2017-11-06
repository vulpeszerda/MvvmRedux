package com.vulpeszerda.mvvmredux.sample

import android.app.ProgressDialog
import android.support.annotation.UiThread
import com.vulpeszerda.mvvmredux.ReduxActivity
import com.vulpeszerda.mvvmredux.ReduxActivityStateView
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 11. 6..
 */
abstract class BaseActivityStateView<T, E : ReduxEvent>(
        activity: ReduxActivity,
        errorHandler: (Throwable) -> Unit) :
        ReduxActivityStateView<T, E>(activity, errorHandler) {

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(activity)
    }

    @UiThread
    open fun showProgressDialog(title: String?, message: String? = null) {
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