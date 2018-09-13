package com.vulpeszerda.mvvmredux.addon

import io.reactivex.*
import org.reactivestreams.Publisher

class BufferUntilTransformer<T, E> private constructor(
    private val events: Observable<E>,
    private val predicate: (E) -> Boolean
) :
    ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer {

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.toFlowable(BackpressureStrategy.BUFFER)
            .concatMap {
                events.filter(predicate)
                    .take(1)
                    .map { _ -> it }
                    .toFlowable(BackpressureStrategy.LATEST)
            }
            .toObservable()

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream.concatMap {
            events.filter(predicate)
                .take(1)
                .map { _ -> it }
                .toFlowable(BackpressureStrategy.LATEST)
        }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> =
        upstream.toFlowable()
            .concatMap {
                events.filter(predicate)
                    .take(1)
                    .map { _ -> it }
                    .toFlowable(BackpressureStrategy.LATEST)
            }
            .firstElement()

    override fun apply(upstream: Completable): CompletableSource =
        upstream.andThen(
            events.filter(predicate)
                .take(1)
                .ignoreElements()
        )


    companion object {

        @JvmStatic
        fun <T, E> create(
            events: Observable<E>,
            predicate: (E) -> Boolean
        ): BufferUntilTransformer<T, E> =
            BufferUntilTransformer(events, predicate)
    }

}