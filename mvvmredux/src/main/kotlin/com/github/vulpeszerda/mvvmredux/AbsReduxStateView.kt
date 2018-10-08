package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.Lifecycle
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.github.vulpeszerda.mvvmredux.addon.filterOnResumed
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
            .filterOnResumed(owner)
            .observeOn(diffScheduler)
            .filter { available && containerView != null }
            .compose { stream ->
                val shared = stream.share()
                val consumers = stateConsumers
                Observable.merge(consumers.map { consumer ->
                    shared.compose(
                        StateConsumerTransformer(consumer) { throwable ->
                            onStateConsumerError(consumer, throwable)
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