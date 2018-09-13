package com.vulpeszerda.mvvmredux.addon

import io.reactivex.*
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher

class FilterTransformer<T, E> private constructor(
    private val events: Observable<E>,
    private val predicate: (E) -> Boolean
) :
    ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    MaybeTransformer<T, T> {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return Observable.defer {
            var lastEvent: E? = null
            var pendedData: DataWrapper<T>? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Observable.merge(
                upstream
                    .filter { data ->
                        lastEvent.let { it != null && predicate.invoke(it) }
                            .apply {
                                pendedData = if (!this) {
                                    DataWrapper(data)
                                } else {
                                    null
                                }
                            }
                    }
                    .map { DataWrapper(it) }
                    .doOnComplete {
                        if (pendedData == null) {
                            disposeSubject.onNext(Unit)
                        }
                        completed = true
                    },
                events.doOnNext { lastEvent = it }
                    .filter(predicate)
                    .flatMap {
                        pendedData?.let { data -> Observable.just(data) } ?: Observable.empty()
                    }
                    .takeUntil { completed }
                    .takeUntil(disposeSubject))
                .distinctUntilChanged { prev, curr -> prev === curr }
                .map { it.data }
        }
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return Flowable.defer {
            var lastEvent: E? = null
            var pendedData: DataWrapper<T>? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Flowable.merge(
                upstream
                    .filter { data ->
                        lastEvent.let { it != null && predicate.invoke(it) }
                            .apply {
                                pendedData = if (!this) {
                                    DataWrapper(data)
                                } else {
                                    null
                                }
                            }
                    }
                    .map {
                        DataWrapper(it)
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
                        pendedData?.let { data -> Flowable.just(data) } ?: Flowable.empty()
                    }
                    .takeUntil { completed }
                    .takeUntil(disposeSubject.toFlowable(BackpressureStrategy.LATEST)))
                .distinctUntilChanged { prev, curr -> prev === curr }
                .map { it.data }
        }
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return Maybe.defer {
            var lastEvent: E? = null
            var pendedData: DataWrapper<T>? = null
            var completed = false
            val disposeSubject = PublishSubject.create<Unit>()
            Observable.merge(
                upstream.toObservable()
                    .filter { data ->
                        lastEvent.let { it != null && predicate.invoke(it) }
                            .apply {
                                pendedData = if (!this) {
                                    DataWrapper(data)
                                } else {
                                    null
                                }
                            }
                    }
                    .map { DataWrapper(it) }
                    .doOnComplete {
                        if (pendedData == null) {
                            disposeSubject.onNext(Unit)
                        }
                        completed = true
                    },
                events.doOnNext { lastEvent = it }
                    .filter(predicate)
                    .flatMap {
                        pendedData?.let { data -> Observable.just(data) } ?: Observable.empty()
                    }
                    .takeUntil { completed }
                    .takeUntil(disposeSubject))
                .distinctUntilChanged { prev, curr -> prev === curr }
                .map { it.data }
                .firstElement()
        }
    }

    private class DataWrapper<out T>(val data: T)

    companion object {

        @JvmStatic
        fun <T, E> create(
            events: Observable<E>,
            predicate: (E) -> Boolean
        ): FilterTransformer<T, E> =
            FilterTransformer(events, predicate)
    }
}