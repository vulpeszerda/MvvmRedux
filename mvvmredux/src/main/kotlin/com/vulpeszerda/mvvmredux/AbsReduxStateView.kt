package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.vulpeszerda.mvvmredux.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by vulpes on 2017. 9. 21..
 */
@Suppress("unused")
abstract class AbsReduxStateView<T>(
    protected val tag: String,
    contextService: ContextService,
    private val diffScheduler: Scheduler = Schedulers.newThread(),
    private val throttle: Long = 0
) : ReduxStateView<T>,
    ContextService by contextService {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    private val stateConsumers = ArrayList<StateConsumer<T>>()

    init {
        @Suppress("LeakingThis")
        contextService.owner.lifecycle.addObserver(this)
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
        }
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

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
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