package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.Lifecycle
import com.github.vulpeszerda.mvvmredux.addon.filterOnStarted
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class AbsReduxStateView<T>(
    protected val tag: String,
    contextDelegate: ContextDelegate,
    private val diffScheduler: Scheduler = Schedulers.newThread(),
    private val throttle: Long = 0
) : ReduxComponent.Impl(contextDelegate),
    ReduxStateView<T> {

    private val stateConsumers = ArrayList<StateConsumer<T>>()

    protected fun <A> addCompletableConsumer(
        prop1: (T) -> A,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createCompletable(prop1, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun <A> addConsumer(
        prop1: (T) -> A,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.create(prop1, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B> addCompletableConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createCompletable(prop1, prop2, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B> addConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.create(prop1, prop2, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C> addCompletableConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createCompletable(prop1, prop2, prop3, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C> addConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, apply
        )
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C, D> addCompletableConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createCompletable(
            prop1, prop2, prop3, prop4, apply
        )
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C, D> addConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, prop4, apply
        )
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C, D, E> addCompletableConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        prop5: (T) -> E,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createCompletable(
            prop1, prop2, prop3, prop4, prop5, apply
        )
        addConsumer(consumer)
        return consumer
    }

    protected fun <A, B, C, D, E> addConsumer(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        prop5: (T) -> E,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, prop4, prop5, apply
        )
        addConsumer(consumer)
        return consumer
    }

    protected fun addDiffConsumer(
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Unit
    ): StateConsumer<T> {
        val consumer = StateConsumer.createDiff(diff, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun addDiffCompletableConsumer(
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Completable
    ): StateConsumer<T> {
        val consumer = StateConsumer.createDiffCompletable(diff, apply)
        addConsumer(consumer)
        return consumer
    }

    protected fun addConsumer(consumer: StateConsumer<T>) {
        synchronized(stateConsumers) {
            stateConsumers.add(consumer)
        }
    }

    protected fun removeConsumer(consumer: StateConsumer<T>) {
        synchronized(stateConsumers) {
            stateConsumers.remove(consumer)
        }
    }


    override fun subscribe(source: Observable<T>): Disposable =
        (if (throttle > 0) source.throttleLast(throttle, TimeUnit.MILLISECONDS) else source)
            .filterOnStarted(owner)
            .observeOn(diffScheduler)
            .filter { available && containerView != null }
            .compose { stream ->
                val shared = stream.share()
                val consumers = stateConsumers
                Observable.merge(consumers.map { consumer ->
                    shared.compose(
                        StateConsumerTransformer(consumer) { throwable ->
                            onStateConsumerError(consumer, throwable)
                            true
                        })
                })
            }
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe({ }) {
                ReduxFramework.onFatalError(it, tag)
            }

    protected open fun onStateConsumerError(consumer: StateConsumer<T>, throwable: Throwable) {
        ReduxFramework.onFatalError(throwable, tag)
    }
}