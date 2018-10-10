package com.github.vulpeszerda.mvvmredux

import io.reactivex.Completable

interface StateConsumer<in T> {

    fun hasChange(prev: T, curr: T): Boolean

    fun apply(prev: T?, curr: T): Completable

    companion object {

        fun <T, A> createCompletable(
            prop1: (T) -> A,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            createDiffCompletable({ prev, curr -> prop1(prev) !== prop1(curr) }, apply)

        fun <T, A> create(
            prop1: (T) -> A,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiff(
                { prev, curr -> prop1(prev) !== prop1(curr) },
                apply
            )

        fun <T, A, B> createCompletable(
            prop1: (T) -> A,
            prop2: (T) -> B,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            createDiffCompletable(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr)
                },
                apply
            )

        fun <T, A, B> create(
            prop1: (T) -> A,
            prop2: (T) -> B,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiff(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr)
                },
                apply
            )

        fun <T, A, B, C> createCompletable(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            createDiffCompletable(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr)
                },
                apply
            )

        fun <T, A, B, C> create(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiff(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr)
                },
                apply
            )

        fun <T, A, B, C, D> createCompletable(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            prop4: (T) -> D,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            createDiffCompletable(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr) ||
                            prop4(prev) !== prop4(curr)
                },
                apply
            )

        fun <T, A, B, C, D> create(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            prop4: (T) -> D,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiff(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr) ||
                            prop4(prev) !== prop4(curr)
                },
                apply
            )

        fun <T, A, B, C, D, E> createCompletable(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            prop4: (T) -> D,
            prop5: (T) -> E,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            createDiffCompletable(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr) ||
                            prop4(prev) !== prop4(curr) ||
                            prop5(prev) !== prop5(curr)
                },
                apply
            )

        fun <T, A, B, C, D, E> create(
            prop1: (T) -> A,
            prop2: (T) -> B,
            prop3: (T) -> C,
            prop4: (T) -> D,
            prop5: (T) -> E,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiff(
                { prev, curr ->
                    prop1(prev) !== prop1(curr) ||
                            prop2(prev) !== prop2(curr) ||
                            prop3(prev) !== prop3(curr) ||
                            prop4(prev) !== prop4(curr) ||
                            prop5(prev) !== prop5(curr)
                },
                apply
            )

        fun <T> createDiffCompletable(
            hasChange: (T, T) -> Boolean,
            apply: (T?, T) -> Completable
        ): StateConsumer<T> =
            object : StateConsumer<T> {

                override fun hasChange(prev: T, curr: T): Boolean =
                    hasChange(prev, curr)

                override fun apply(prev: T?, curr: T) =
                    apply(prev, curr)
            }

        fun <T> createDiff(
            hasChange: (T, T) -> Boolean,
            apply: (T?, T) -> Unit
        ): StateConsumer<T> =
            createDiffCompletable(hasChange) { prev, curr ->
                Completable.fromAction { apply.invoke(prev, curr) }
            }
    }
}