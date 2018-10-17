package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.LifecycleOwner
import io.reactivex.Completable
import io.reactivex.disposables.Disposable

interface ReduxStateView {

    val owner: LifecycleOwner
    val canUpdateView: Boolean

    fun <T, A> ReduxViewModel<T>.subscribeConsumer(
        prop1: (T) -> A,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A> ReduxViewModel<T>.subscribe(
        prop1: (T) -> A,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(prop1, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B> ReduxViewModel<T>.subscribeCompletable(
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, prop2, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B> ReduxViewModel<T>.subscribe(
        prop1: (T) -> A,
        prop2: (T) -> B,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(prop1, prop2, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C> ReduxViewModel<T>.subscribeCompletable(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(prop1, prop2, prop3, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C> ReduxViewModel<T>.subscribe(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, apply
        )
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C, D> ReduxViewModel<T>.subscribeCompletable(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createCompletable(
            prop1, prop2, prop3, prop4, apply
        )
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C, D> ReduxViewModel<T>.subscribe(
        prop1: (T) -> A,
        prop2: (T) -> B,
        prop3: (T) -> C,
        prop4: (T) -> D,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.create(
            prop1, prop2, prop3, prop4, apply
        )
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C, D, E> ReduxViewModel<T>.subscribeCompletable(
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
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T, A, B, C, D, E> ReduxViewModel<T>.subscribe(
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
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T> ReduxViewModel<T>.subscribeDiff(
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Unit
    ): Disposable {
        val consumer = StateConsumer.createDiff(diff, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    fun <T> ReduxViewModel<T>.subscribeDiffCompletable(
        diff: (T, T) -> Boolean,
        apply: (T?, T) -> Completable
    ): Disposable {
        val consumer = StateConsumer.createDiffCompletable(diff, apply)
        return StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
    }

    private fun <T> ReduxViewModel<T>.subscribe(consumer: StateConsumer<T>): Disposable =
        StateBinder.subscribe(state, owner, { canUpdateView }, consumer)
}