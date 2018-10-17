package com.github.vulpeszerda.mvvmreduxsample

import android.app.ProgressDialog
import androidx.annotation.UiThread
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.ReduxComponent
import com.github.vulpeszerda.mvvmredux.ReduxStateView

open class BaseStateView(
    tag: String,
    contextDelegate: ContextDelegate
) : ReduxComponent by ReduxComponent.Impl(contextDelegate),
    ReduxStateView,
    ContextDelegate by contextDelegate {

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(context)
    }

    @UiThread
    protected fun showProgressDialog(title: String?, message: String? = null) {
        if (available) {
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
    protected fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}