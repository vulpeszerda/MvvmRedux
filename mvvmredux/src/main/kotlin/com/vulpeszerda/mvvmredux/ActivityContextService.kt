package com.vulpeszerda.mvvmredux

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.Toast

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class ActivityContextService(override val activity: ReduxActivity) : ContextService {

    override val owner: LifecycleOwner
        get() = activity

    override val containerView: View? by lazy {
        activity.findViewById(android.R.id.content) as View?
    }

    override val available: Boolean
        get() = !activity.isFinishing

    override val context: Context
        get() = activity

    override val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager

    override fun getActivityOrThrow(): Activity = activity

    override fun getContextOrThrow(): Context = context

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

    override fun recreate() {
        activity.recreate()
    }

    override fun toast(msgResId: Int, duration: ContextService.ToastDuration): Toast =
        toast(context.getString(msgResId), duration)

    override fun toast(msg: String, duration: ContextService.ToastDuration): Toast =
        Toast.makeText(
            context, msg, when (duration) {
                ContextService.ToastDuration.SHORT -> Toast.LENGTH_SHORT
                ContextService.ToastDuration.LONG -> Toast.LENGTH_LONG
            }
        ).apply { show() }
}