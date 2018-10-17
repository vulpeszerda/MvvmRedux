package com.github.vulpeszerda.mvvmredux

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.extensions.LayoutContainer

interface ContextDelegate : LayoutContainer, LifecycleObserver {
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

    val canUpdateView: Boolean
        get () = available && containerView != null

    class ActivityDelegate(override val activity: AppCompatActivity) : ContextDelegate {

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
    }

    @Suppress("MemberVisibilityCanBePrivate", "unused")
    private class FragmentDelegate(private val fragment: Fragment) : ContextDelegate {

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
    }

    companion object {

        fun create(activity: AppCompatActivity): ContextDelegate =
            ActivityDelegate(activity)

        fun create(fragment: Fragment): ContextDelegate =
            FragmentDelegate(fragment)
    }
}