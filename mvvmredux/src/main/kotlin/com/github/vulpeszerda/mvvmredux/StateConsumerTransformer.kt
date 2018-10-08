package com.github.vulpeszerda.mvvmredux

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class StateConsumerTransformer<T>(
    private val consumer: StateConsumer<T>,
    private val errorHandler: (Throwable) -> Unit
) : ObservableTransformer<T, Unit> {

    private var prevState: T? = null

    override fun apply(upstream: Observable<T>): ObservableSource<Unit> =
        upstream.filter { prevState !== it }
            .flatMapCompletable { currState ->
                val prev = prevState
                val completable = if (prev == null || consumer.hasChange(prev, currState)) {
                    consumer.apply(prev, currState)
                } else {
                    Completable.complete()
                }
                completable
                    .doOnComplete { prevState = currState }
                    .onErrorComplete {
                        errorHandler(it)
                        true
                    }
            }
            .toObservable<Unit>()
}
