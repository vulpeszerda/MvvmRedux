package com.vulpeszerda.mvvmredux.library

import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 8. 30..
 */
interface UiSchedulerProvider {
    val uiScheduler: Scheduler
}