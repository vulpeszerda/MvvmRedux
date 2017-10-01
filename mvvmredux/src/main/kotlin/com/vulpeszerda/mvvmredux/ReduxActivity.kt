package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle

/**
 * Created by vulpes on 2017. 8. 25..
 */
open class ReduxActivity(lifecycleOwner: LifecycleRegistryOwner = AbsLifecycleRegistryOwner()) :
        AppCompatActivity(), LifecycleRegistryOwner by lifecycleOwner {

    protected val rxLifecycleProvider = AndroidLifecycle.createLifecycleProvider(lifecycleOwner)



}