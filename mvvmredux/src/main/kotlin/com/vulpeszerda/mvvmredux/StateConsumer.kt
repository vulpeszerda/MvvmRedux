package com.vulpeszerda.mvvmredux

import io.reactivex.Completable
import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 12. 9..
 */
interface StateConsumer<in T> {

    fun hasChange(prev: T, curr: T): Boolean

    fun apply(prev: T?, curr: T): Completable

    companion object {

        fun <T> create(
            hasChange: (T, T) -> Boolean,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            object : StateConsumer<T> {

                override fun hasChange(prev: T, curr: T): Boolean =
                    hasChange.invoke(prev, curr)

                override fun apply(prev: T?, curr: T) =
                    apply.invoke(prev, curr)
            }

        fun <T> createFromAction(
            hasChange: (T, T) -> Boolean,
            apply: (T?, T) -> Unit,
            scheduler: Scheduler? = null
        ): StateConsumer<T> =
            create(hasChange, { prev, curr ->
                Completable.fromAction { apply.invoke(prev, curr) }
                    .let { if (scheduler != null) it.subscribeOn(scheduler) else it }
            })
    }
}