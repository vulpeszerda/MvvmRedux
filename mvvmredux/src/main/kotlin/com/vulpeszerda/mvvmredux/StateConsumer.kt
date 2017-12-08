package com.vulpeszerda.mvvmredux

import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 12. 9..
 */
interface StateConsumer<in T> {

    val applyScheduler: Scheduler

    fun hasChange(prevState: T?, currState: T?): Boolean

    fun apply(prevState: T?, currState: T?)
}