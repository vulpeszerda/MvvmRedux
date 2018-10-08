package com.github.vulpeszerda.mvvmredux.sample

import android.app.ProgressDialog
import androidx.annotation.UiThread
import com.github.vulpeszerda.mvvmredux.AbsReduxStateView
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmredux.StateConsumer
import io.reactivex.android.schedulers.AndroidSchedulers

open class BaseStateView<T>(
    tag: String = "BaseStateView",
    contextDelegate: ContextDelegate
) : AbsReduxStateView<GlobalState<T>>(tag, contextDelegate, AndroidSchedulers.mainThread()) {

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

    override fun onStateConsumerError(
        consumer: StateConsumer<GlobalState<T>>,
        throwable: Throwable
    ) {
        throwable.printStackTrace()
    }
}