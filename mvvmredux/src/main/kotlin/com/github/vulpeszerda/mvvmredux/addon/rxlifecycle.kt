@file:Suppress("unused")

package com.github.vulpeszerda.mvvmredux.addon

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable

fun <T> Observable<T>.filter(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Observable<T> =
    this.compose(FilterOnLifecycleTransformer.create(owner, from, until))

fun <T> Observable<T>.filterOnResumed(owner: LifecycleOwner): Observable<T> =
    this.compose(FilterOnLifecycleTransformer.createOnResumed(owner))

fun <T> Observable<T>.filterOnStarted(owner: LifecycleOwner): Observable<T> =
    this.compose(
        FilterOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun <T> Flowable<T>.filter(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Flowable<T> =
    this.compose(
        FilterOnLifecycleTransformer.create(
            owner,
            from,
            until
        )
    )

fun <T> Flowable<T>.filterOnResumed(owner: LifecycleOwner): Flowable<T> =
    this.compose(
        FilterOnLifecycleTransformer.createOnResumed(
            owner
        )
    )

fun <T> Flowable<T>.filterOnStarted(owner: LifecycleOwner): Flowable<T> =
    this.compose(
        FilterOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun <T> Maybe<T>.filter(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Maybe<T> =
    this.compose(
        FilterOnLifecycleTransformer.create(
            owner,
            from,
            until
        )
    )

fun <T> Maybe<T>.filterOnResumed(owner: LifecycleOwner): Maybe<T> =
    this.compose(
        FilterOnLifecycleTransformer.createOnResumed(
            owner
        )
    )

fun <T> Maybe<T>.filterOnStarted(owner: LifecycleOwner): Maybe<T> =
    this.compose(
        FilterOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun <T> Observable<T>.bufferUntil(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Observable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.create(
            owner,
            from,
            until
        )
    )

fun <T> Observable<T>.bufferUntilOnResumed(owner: LifecycleOwner): Observable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnResumed(
            owner
        )
    )

fun <T> Observable<T>.bufferUntilOnStarted(owner: LifecycleOwner): Observable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun <T> Flowable<T>.bufferUntil(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Flowable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.create(
            owner,
            from,
            until
        )
    )

fun <T> Flowable<T>.bufferUntilOnResumed(owner: LifecycleOwner): Flowable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnResumed(
            owner
        )
    )

fun <T> Flowable<T>.bufferUntilOnStarted(owner: LifecycleOwner): Flowable<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun <T> Maybe<T>.bufferUntil(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Maybe<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.create(
            owner,
            from,
            until
        )
    )

fun <T> Maybe<T>.bufferUntilOnResumed(owner: LifecycleOwner): Maybe<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnResumed(
            owner
        )
    )

fun <T> Maybe<T>.bufferUntilOnStarted(owner: LifecycleOwner): Maybe<T> =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnStarted(
            owner
        )
    )

fun Completable.bufferUntil(
    owner: LifecycleOwner,
    from: Lifecycle.Event,
    until: Lifecycle.Event
): Completable =
    this.compose(
        BufferUntilOnLifecycleTransformer.create<Any>(
            owner,
            from,
            until
        )
    )

fun Completable.bufferUntilOnResumed(owner: LifecycleOwner): Completable =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnResumed<Any>(
            owner
        )
    )

fun Completable.bufferUntilOnStarted(owner: LifecycleOwner): Completable =
    this.compose(
        BufferUntilOnLifecycleTransformer.createOnStarted<Any>(
            owner
        )
    )
