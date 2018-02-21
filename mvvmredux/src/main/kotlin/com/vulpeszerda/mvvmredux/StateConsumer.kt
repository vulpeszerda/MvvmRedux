package com.vulpeszerda.mvvmredux

import io.reactivex.Completable
import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 12. 9..
 */
interface StateConsumer<in T> {

    fun hasChange(prevState: T?, currState: T?): Boolean

    fun apply(prevState: T?, currState: T?): Completable

    companion object {

        fun <T> create(
            hasChange: (T?, T?) -> Boolean,
            apply: (T?, T?) -> Completable
        ): StateConsumer<T> =
            object : StateConsumer<T> {

                override fun hasChange(prevState: T?, currState: T?): Boolean =
                    hasChange.invoke(prevState, currState)

                override fun apply(prevState: T?, currState: T?) =
                    apply.invoke(prevState, currState)
            }

        fun <T> createFromAction(
            hasChange: (T?, T?) -> Boolean,
            apply: (T?, T?) -> Unit,
            scheduler: Scheduler? = null
        ): StateConsumer<T> =
            create(hasChange, { prev, curr ->
                Completable.fromAction { apply.invoke(prev, curr) }
                    .let { if (scheduler != null) it.subscribeOn(scheduler) else it }
            })
    }
}