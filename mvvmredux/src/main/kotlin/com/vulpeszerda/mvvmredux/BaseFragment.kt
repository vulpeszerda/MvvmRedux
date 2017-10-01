package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v4.app.Fragment
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle

/**
 * Created by vulpes on 2017. 8. 25..
 */
open class BaseFragment(lifecycleOwner: LifecycleRegistryOwner = AbsLifecycleRegistryOwner()) :
        Fragment(), LifecycleRegistryOwner by lifecycleOwner {

    protected val rxLifecycleProvider = AndroidLifecycle.createLifecycleProvider(lifecycleOwner)

}