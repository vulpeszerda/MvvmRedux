package com.vulpeszerda.mvvmredux

import android.app.Activity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentManager
import android.widget.Toast
import kotlinx.android.extensions.LayoutContainer

interface ContextService : LayoutContainer, LifecycleObserver {
    val owner: LifecycleOwner
    val available: Boolean
    val context: Context?
    val activity: Activity?
    val fragmentManager: FragmentManager
    fun getContextOrThrow(): Context
    fun getActivityOrThrow(): Activity
    fun startActivity(intent: Intent, requestCode: Int? = null)
    fun setResult(resultCode: Int, data: Intent? = null)
    fun finish()
    fun finishAffinity()
    fun recreate()
    fun toast(msgResId: Int, duration: ToastDuration = ToastDuration.SHORT): Toast?
    fun toast(msg: String, duration: ToastDuration = ToastDuration.SHORT): Toast?

    enum class ToastDuration {
        SHORT,
        LONG
    }
}