package com.github.vulpeszerda.mvvmredux

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
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

    val extras: Bundle?

    fun getContextOrThrow(): Context

    fun getActivityOrThrow(): Activity

    fun startActivity(intent: Intent, requestCode: Int? = null, options: Bundle? = null)

    fun startActivities(vararg intents: Intent, options: Bundle? = null)

    fun startIntentSenderForResult(
        intentSender: IntentSender,
        requestCode: Int,
        fillInIntent: Intent? = null,
        flagsMask: Int = 0,
        flagsValues: Int = 0,
        extraFlags: Int = 0,
        options: Bundle? = null
    )

    fun setResult(resultCode: Int, data: Intent? = null)

    fun finish()

    fun finishAffinity()

    fun recreate()

    private class ActivityDelegate(override val activity: AppCompatActivity) : ContextDelegate {

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

        override val extras: Bundle?
            get() = activity.intent?.extras

        override fun getActivityOrThrow(): Activity = activity

        override fun getContextOrThrow(): Context = context

        override fun startActivity(intent: Intent, requestCode: Int?, options: Bundle?) {
            if (requestCode != null) {
                activity.startActivityForResult(intent, requestCode, options)
            } else {
                activity.startActivity(intent, options)
            }
        }

        override fun startIntentSenderForResult(
            intentSender: IntentSender,
            requestCode: Int,
            fillInIntent: Intent?,
            flagsMask: Int,
            flagsValues: Int,
            extraFlags: Int,
            options: Bundle?
        ) {
            activity.startIntentSenderForResult(
                intentSender,
                requestCode,
                fillInIntent,
                flagsMask,
                flagsValues,
                extraFlags,
                options
            )
        }

        override fun startActivities(vararg intents: Intent, options: Bundle?) {
            activity.startActivities(intents, options)
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
    private class FragmentDelegate(val fragment: Fragment) : ContextDelegate {

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

        override val extras: Bundle?
            get() = fragment.arguments

        override fun getActivityOrThrow(): Activity = fragment.requireActivity()

        override fun getContextOrThrow(): Context = fragment.requireContext()

        override fun startActivity(intent: Intent, requestCode: Int?, options: Bundle?) {
            if (requestCode != null) {
                fragment.startActivityForResult(intent, requestCode, options)
            } else {
                fragment.startActivity(intent, options)
            }
        }

        override fun startIntentSenderForResult(
            intentSender: IntentSender,
            requestCode: Int,
            fillInIntent: Intent?,
            flagsMask: Int,
            flagsValues: Int,
            extraFlags: Int,
            options: Bundle?
        ) {
            fragment.startIntentSenderForResult(
                intentSender,
                requestCode,
                fillInIntent,
                flagsMask,
                flagsValues,
                extraFlags,
                options
            )
        }

        override fun startActivities(vararg intents: Intent, options: Bundle?) {
            val activity = fragment.activity
                ?: throw IllegalStateException("Fragment $fragment not attached to Activity")
            activity.startActivities(intents, options)
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