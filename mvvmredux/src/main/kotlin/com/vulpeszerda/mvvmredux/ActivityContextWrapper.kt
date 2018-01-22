package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.view.View

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class ActivityContextWrapper(protected val activity: ReduxActivity) : ContextWrapper {

    override val owner: LifecycleOwner
        get() = activity

    override val containerView: View? by lazy {
        activity.findViewById(android.R.id.content) as View?
    }

    override val available: Boolean
        get() = !activity.isFinishing

    override val context: Context
        get() = activity

    override fun startActivity(intent: Intent, requestCode: Int?) {
        if (requestCode != null) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivity(intent)
        }
    }

    override fun setResult(resultCode: Int, data: Intent?) {
        activity.setResult(resultCode, data)
    }

    override fun finish() {
        activity.finish()
    }

    override fun finishAffinity() {
        ActivityCompat.finishAffinity(activity)
    }

}