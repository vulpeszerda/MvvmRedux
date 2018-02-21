package com.vulpeszerda.mvvmredux.sample

import android.app.ProgressDialog
import android.support.annotation.UiThread
import com.vulpeszerda.mvvmredux.*
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by vulpes on 2017. 11. 23..
 */
abstract class BaseStateView<T>(
    tag: String,
    contextWrapper: ContextService
) : AbsReduxStateView<T>(tag, contextWrapper, AndroidSchedulers.mainThread()) {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextService(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextService(fragment))

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

    override fun onStateConsumerError(consumer: StateConsumer<T>, throwable: Throwable) {
        throwable.printStackTrace()
    }
}