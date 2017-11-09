package com.vulpeszerda.mvvmredux.sample

import android.app.ProgressDialog
import android.support.annotation.UiThread
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.ReduxFragment
import com.vulpeszerda.mvvmredux.ReduxFragmentStateView

/**
 * Created by vulpes on 2017. 11. 6..
 */
abstract class BaseFragmentStateView<T>(
        tag: String,
        fragment: ReduxFragment) :
        ReduxFragmentStateView<T>(tag, fragment) {

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(fragment.context)
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
