package com.vulpeszerda.mvvmredux

import com.vulpeszerda.mvvmredux.addon.filterOnStarted
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsReduxStateView<T>(
        protected val tag: String,
        contextWrapper: ContextWrapper,
        private val diffScheduler: Scheduler = Schedulers.newThread(),
        private val throttle: Long = 0) :
        ReduxStateView<T>,
        ContextWrapper by contextWrapper {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextWrapper(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextWrapper(fragment))

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    protected val stateConsumers = ArrayList<StateConsumer<T>>()

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<T>): Disposable =
            (if (throttle > 0) source.throttleLast(throttle, TimeUnit.MILLISECONDS) else source)
                    .filterOnStarted(owner)
                    .observeOn(diffScheduler)
                    .distinctUntilChanged()
                    .scan(StatePair<T>(null, null))
                    { prevPair, curr -> StatePair(prevPair.curr, curr) }
                    .filter { (prev, curr) -> prev !== curr }
                    .flatMapCompletable { (prev, curr) ->
                        Completable.merge(stateConsumers.mapNotNull { consumer ->
                            if (consumer.hasChange(prev, curr)) {
                                Completable.fromAction { consumer.apply(prev, curr) }
                                        .subscribeOn(consumer.applyScheduler)
                            } else {
                                null
                            }
                        })
                    }
                    .subscribe({ }) { publishEvent(ReduxEvent.Error(it, tag)) }
}