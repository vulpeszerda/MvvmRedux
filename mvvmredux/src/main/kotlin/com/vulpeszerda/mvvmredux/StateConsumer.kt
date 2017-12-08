package com.vulpeszerda.mvvmredux

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by vulpes on 2017. 12. 9..
 */
interface StateConsumer<in T> {

    val applyScheduler: Scheduler

    fun hasChange(prevState: T?, currState: T?): Boolean

    fun apply(prevState: T?, currState: T?)

    companion object {

        fun <T> create(hasChange: (T?, T?) -> Boolean,
                       apply: (T?, T?) -> Unit,
                       applyScheduler: Scheduler = AndroidSchedulers.mainThread()):
                StateConsumer<T> = object : StateConsumer<T> {
            override val applyScheduler: Scheduler
                get() = applyScheduler

            override fun hasChange(prevState: T?, currState: T?): Boolean =
                    hasChange.invoke(prevState, currState)

            override fun apply(prevState: T?, currState: T?) =
                    apply.invoke(prevState, currState)
        }
    }
}