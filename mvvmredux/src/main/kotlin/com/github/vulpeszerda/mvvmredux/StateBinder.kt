package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.github.vulpeszerda.mvvmredux.addon.filterOnResumed
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object StateBinder {

    private val DEFAULT_DIFF_SCHEDULER = AndroidSchedulers.mainThread()
    var globalExceptionReporter: ((Throwable) -> Unit)? = null

    fun <T, A> subscribeConsumer(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(prop1, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B> subscribeCompletable(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, prop2, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(prop1, prop2, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C> subscribeCompletable(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, prop2, prop3, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, apply
        )
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C, D> subscribeCompletable(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(
            prop1, prop2, prop3, prop4, apply
        )
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C, D> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, prop4, apply
        )
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C, D, E> subscribeCompletable(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        prop5: (T) -> E,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(
            prop1, prop2, prop3, prop4, prop5, apply
        )
        return subscribe(source, owner, available, consumer)
    }

    fun <T, A, B, C, D, E> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        prop5: (T) -> E,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, prop4, prop5, apply
        )
        return subscribe(source, owner, available, consumer)
    }

    fun <T> subscribeDiff(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.createDiff(diff, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T> subscribeDiffCompletable(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createDiffCompletable(diff, apply)
        return subscribe(source, owner, available, consumer)
    }

    fun <T> subscribe(
        source: Observable<T>,
        owner: LifecycleOwner,
        available: () -> Boolean,
        consumer: StateConsumer<T>,
        diffScheduler: Scheduler = DEFAULT_DIFF_SCHEDULER,
        throttle: Long = 0
    ): Disposable =
        (if (throttle > 0) source.throttleLast(throttle, TimeUnit.MILLISECONDS) else source)
            .filterOnResumed(owner)
            .observeOn(diffScheduler)
            .filter { available() }
            .compose(StateConsumerTransformer(consumer) { throwable ->
                globalExceptionReporter?.invoke(throwable)
            })
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe({ }) {
                ReduxFramework.onFatalError(it)
            }
}