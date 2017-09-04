package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleRegistryOwner
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by vulpes on 2017. 8. 25..
 */
open class BaseFragment(lifecycleOwner: LifecycleRegistryOwner = LifecycleOwner()) :
        RxFragment(), LifecycleRegistryOwner by lifecycleOwner, UiSchedulerProvider {

    override val uiScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()

}