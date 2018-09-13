package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@Suppress("unused")
class CompositeReduxStateView<T>(
    vararg components: ReduxStateView<T>
) : ReduxStateView<T> {

    private val _components = components
    private val publisher = ReduxEventPublisher.Impl()

    override val events: Observable<ReduxEvent> =
        Observable.merge(_components.map { it.events })
            .mergeWith(publisher.events)

    override fun bindToLifecycle() {
        _components.forEach { it.bindToLifecycle() }
    }

    override fun subscribe(source: Observable<T>): Disposable =
        CompositeDisposable(_components.map { it.subscribe(source) })

    override fun publishEvent(event: ReduxEvent) {
        publisher.publishEvent(event)
    }

}