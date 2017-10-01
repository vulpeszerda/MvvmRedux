package com.vulpeszerda.mvvmredux.addon

import io.reactivex.*
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher

/**
 * Created by vulpes on 2017. 9. 19..
 */
class FilterTransformer<T, E> private constructor(
        private val events: Observable<E>,
        private val predicate: (E) -> Boolean) :
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return Observable.defer {
            var lastEvent: E? = null
            var pendedData: T? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Observable.merge(
                    upstream
                            .filter { data ->
                                lastEvent.let { it != null && predicate.invoke(it) }
                                        .apply { pendedData = if (!this) data else null }
                            }
                            .doOnComplete {
                                if (pendedData == null) {
                                    disposeSubject.onNext(Unit)
                                }
                                completed = true
                            },
                    events.doOnNext { lastEvent = it }
                            .filter(predicate)
                            .flatMap {
                                pendedData.let {
                                    if (it == null) Observable.empty() else Observable.just(it)
                                }
                            }
                            .takeUntil { completed }
                            .takeUntil(disposeSubject))
                    .distinctUntilChanged()
        }
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return Flowable.defer {
            var lastEvent: E? = null
            var pendedData: T? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Flowable.merge(
                    upstream
                            .filter { data ->
                                lastEvent.let { it != null && predicate.invoke(it) }
                                        .apply { pendedData = if (!this) data else null }
                            }
                            .doOnComplete {
                                if (pendedData == null) {
                                    disposeSubject.onNext(Unit)
                                }
                                completed = true
                            },
                    events.toFlowable(BackpressureStrategy.LATEST)
                            .doOnNext { lastEvent = it }
                            .filter(predicate)
                            .flatMap {
                                pendedData.let {
                                    if (it == null) Flowable.empty() else Flowable.just(it)
                                }
                            }
                            .takeUntil { completed }
                            .takeUntil(disposeSubject.toFlowable(BackpressureStrategy.LATEST)))
                    .distinctUntilChanged()
        }
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return Maybe.defer {
            var lastEvent: E? = null
            var pendedData: T? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Observable.merge(
                    upstream.toObservable()
                            .filter { data ->
                                lastEvent.let { it != null && predicate.invoke(it) }
                                        .apply { pendedData = if (!this) data else null }
                            }
                            .doOnComplete {
                                if (pendedData == null) {
                                    disposeSubject.onNext(Unit)
                                }
                                completed = true
                            },
                    events.doOnNext { lastEvent = it }
                            .filter(predicate)
                            .flatMap {
                                pendedData.let {
                                    if (it == null) Observable.empty() else Observable.just(it)
                                }
                            }
                            .takeUntil { completed }
                            .takeUntil(disposeSubject))
                    .distinctUntilChanged()
                    .firstElement()
        }
    }

    companion object {

        @JvmStatic
        fun <T, E> create(events: Observable<E>,
                          predicate: (E) -> Boolean): FilterTransformer<T, E> =
                FilterTransformer(events, predicate)
    }
}