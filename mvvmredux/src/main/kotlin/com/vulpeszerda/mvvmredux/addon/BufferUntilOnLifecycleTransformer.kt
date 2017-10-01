package com.vulpeszerda.mvvmredux.addon

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import org.reactivestreams.Publisher

/**
 * Created by vulpes on 2017. 9. 19..
 */
class BufferUntilOnLifecycleTransformer<T> private constructor(
        owner: LifecycleOwner,
        private val from: Lifecycle.Event,
        private val until: Lifecycle.Event,
        private val scheduler: Scheduler? = null) :
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    private val provider = AndroidLifecycle.createLifecycleProvider(owner)

    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream
            .compose<T>(BufferUntilTransformer.create(
                    provider.lifecycle()) { event ->
                val curr = convertEventAsInt(
                        event)
                val f = convertEventAsInt(
                        from)
                val u = convertEventAsInt(
                        until)
                curr in f..(u - 1)
            })
            .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
            .let { if (scheduler != null) it.observeOn(scheduler) else it }

    override fun apply(upstream: Flowable<T>): Publisher<T> = upstream
            .compose<T>(BufferUntilTransformer.create(
                    provider.lifecycle()) { event ->
                val curr = convertEventAsInt(
                        event)
                val f = convertEventAsInt(
                        from)
                val u = convertEventAsInt(
                        until)
                curr in f..(u - 1)
            })
            .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
            .let { if (scheduler != null) it.observeOn(scheduler) else it }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream
            .compose<T>(BufferUntilTransformer.create(
                    provider.lifecycle()) { event ->
                val curr = convertEventAsInt(
                        event)
                val f = convertEventAsInt(
                        from)
                val u = convertEventAsInt(
                        until)
                curr in f..(u - 1)
            })
            .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
            .let { if (scheduler != null) it.observeOn(scheduler) else it }

    override fun apply(upstream: Completable): CompletableSource = upstream
            .compose(BufferUntilTransformer.create<T, Lifecycle.Event>(
                    provider.lifecycle()) { event ->
                val curr = convertEventAsInt(
                        event)
                val f = convertEventAsInt(
                        from)
                val u = convertEventAsInt(
                        until)
                curr in f..(u - 1)
            })
            .compose(provider.bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY))
            .let { if (scheduler != null) it.observeOn(scheduler) else it }

    companion object {

        @JvmStatic
        fun <T> create(owner: LifecycleOwner,
                       from: Lifecycle.Event,
                       until: Lifecycle.Event,
                       scheduler: Scheduler? = AndroidSchedulers.mainThread()):
                BufferUntilOnLifecycleTransformer<T> =
                BufferUntilOnLifecycleTransformer(owner,
                        from,
                        until,
                        scheduler)

        fun <T> createOnResumed(owner: LifecycleOwner,
                                scheduler: Scheduler? = AndroidSchedulers.mainThread()):
                BufferUntilOnLifecycleTransformer<T> =
                BufferUntilOnLifecycleTransformer(owner,
                        Lifecycle.Event.ON_RESUME,
                        Lifecycle.Event.ON_PAUSE,
                        scheduler)

        fun <T> createOnStarted(owner: LifecycleOwner,
                                scheduler: Scheduler? = AndroidSchedulers.mainThread()):
                BufferUntilOnLifecycleTransformer<T> =
                BufferUntilOnLifecycleTransformer(owner,
                        Lifecycle.Event.ON_START,
                        Lifecycle.Event.ON_STOP,
                        scheduler)

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