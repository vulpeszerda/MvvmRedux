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
open class FragmentContextService(protected val fragment: ReduxFragment) : ContextService {

    override val owner: LifecycleOwner
        get() = fragment

    override val containerView: View?
        get() = fragment.view

    override val available: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false &&
                context != null && activity != null

    override val context: Context?
        get() = fragment.context

    override val activity: Activity?
        get() = fragment.activity

    override val fragmentManager: FragmentManager
        get() = fragment.childFragmentManager

    override fun getActivityOrThrow(): Activity = activity!!

    override fun getContextOrThrow(): Context = context!!

    override fun startActivity(intent: Intent, requestCode: Int?) {
        if (requestCode != null) {
            fragment.startActivityForResult(intent, requestCode)
        } else {
            fragment.startActivity(intent)
        }
    }

    override fun setResult(resultCode: Int, data: Intent?) {
        fragment.activity?.setResult(resultCode, data)
    }

    override fun finish() {
        fragment.activity?.finish()
    }

    override fun finishAffinity() {
        fragment.activity?.run { ActivityCompat.finishAffinity(this) }
    }

    override fun recreate() {
        activity?.recreate()
    }

    override fun toast(msgResId: Int, duration: ContextService.ToastDuration): Toast? {
        val context = context ?: return null
        return toast(context.getString(msgResId), duration)
    }

    override fun toast(msg: String, duration: ContextService.ToastDuration): Toast? {
        val context = context ?: return null
        return Toast.makeText(
            context, msg, when (duration) {
                ContextService.ToastDuration.SHORT -> Toast.LENGTH_SHORT
                ContextService.ToastDuration.LONG -> Toast.LENGTH_LONG
            }
        ).apply { show() }
    }

}