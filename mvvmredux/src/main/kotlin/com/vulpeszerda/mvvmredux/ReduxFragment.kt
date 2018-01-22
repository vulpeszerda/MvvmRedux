package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.support.v4.app.Fragment
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle2.LifecycleProvider

/**
 * Created by vulpes on 2017. 8. 25..
 */
open class ReduxFragment : Fragment() {

    val rxLifecycleProvider: LifecycleProvider<Lifecycle.Event> by lazy {
        AndroidLifecycle.createLifecycleProvider(this)
    }
}