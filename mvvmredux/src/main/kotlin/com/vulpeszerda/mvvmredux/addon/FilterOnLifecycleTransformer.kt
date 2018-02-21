package com.vulpeszerda.mvvmredux.addon

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * Created by vulpes on 2017. 9. 19..
 */
class FilterOnLifecycleTransformer<T> private constructor(
    owner: LifecycleOwner,
    private val from: Lifecycle.Event,
    private val until: Lifecycle.Event
) :
    ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    MaybeTransformer<T, T> {

    private val provider = AndroidLifecycle.createLifecycleProvider(owner)

    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream
        .compose<T>(FilterTransformer.create(provider.lifecycle()) { event ->
            val curr = convertEventAsInt(
                event
            )
            val f = convertEventAsInt(
                from
            )
            val u = convertEventAsInt(
                until
            )
            curr in f..(u - 1)
        })
        .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))

    override fun apply(upstream: Flowable<T>): Publisher<T> = upstream
        .compose<T>(FilterTransformer.create(provider.lifecycle()) { event ->
            val curr = convertEventAsInt(
                event
            )
            val f = convertEventAsInt(
                from
            )
            val u = convertEventAsInt(
                until
            )
            curr in f..(u - 1)
        })
        .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))

    override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream
        .compose<T>(FilterTransformer.create(provider.lifecycle()) { event ->
            val curr = convertEventAsInt(
                event
            )
            val f = convertEventAsInt(
                from
            )
            val u = convertEventAsInt(
                until
            )
            curr in f..(u - 1)
        })
        .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))

    companion object {

        @JvmStatic
        fun <T> create(
            owner: LifecycleOwner,
            from: Lifecycle.Event,
            until: Lifecycle.Event
        ):
                FilterOnLifecycleTransformer<T> =
            FilterOnLifecycleTransformer(owner, from, until)

        fun <T> createOnResumed(owner: LifecycleOwner):
                FilterOnLifecycleTransformer<T> =
            FilterOnLifecycleTransformer(
                owner, Lifecycle.Event.ON_RESUME, Lifecycle.Event.ON_PAUSE
            )

        fun <T> createOnStarted(owner: LifecycleOwner):
                FilterOnLifecycleTransformer<T> =
            FilterOnLifecycleTransformer(
                owner, Lifecycle.Event.ON_START, Lifecycle.Event.ON_STOP
            )

        private fun convertEventAsInt(event: Lifecycle.Event): Int = when (event) {
            Lifecycle.Event.ON_CREATE -> 0
            Lifecycle.Event.ON_START -> 1
            Lifecycle.Event.ON_RESUME -> 2
            Lifecycle.Event.ON_PAUSE -> 3
            Lifecycle.Event.ON_STOP -> 4
            Lifecycle.Event.ON_DESTROY -> 5
            else -> 999
        }
    }
}