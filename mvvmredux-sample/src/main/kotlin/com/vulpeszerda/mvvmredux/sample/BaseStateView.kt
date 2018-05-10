package com.vulpeszerda.mvvmredux.sample

import android.app.ProgressDialog
import android.support.annotation.UiThread
import com.vulpeszerda.mvvmredux.AbsReduxStateView
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.StateConsumer
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class BaseStateView<T>(
    tag: String = "BaseStateView",
    contextService: ContextService
) : AbsReduxStateView<GlobalState<T>>(tag, contextService, AndroidSchedulers.mainThread()) {

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