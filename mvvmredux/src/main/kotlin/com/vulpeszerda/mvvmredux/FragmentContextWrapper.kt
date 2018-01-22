package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.view.View

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class FragmentContextWrapper(protected val fragment: ReduxFragment) : ContextWrapper {

    override val owner: LifecycleOwner
        get() = fragment

    override val containerView: View?
        get() = fragment.view

    override val available: Boolean
        get() = fragment.isAdded && fragment.activity?.isFinishing == false

    override val context: Context
        get() = fragment.context!!

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
}