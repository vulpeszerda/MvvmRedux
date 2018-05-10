package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CompositeReduxErrorHandler(
    vararg components: ReduxErrorHandler
) : ReduxErrorHandler {

    private val _components = components
    private val publisher = ReduxEventPublisher.Impl()

    override val events: Observable<ReduxEvent> =
        Observable.merge(_components.map { it.events })
            .mergeWith(publisher.events)

    override fun onError(error: ReduxEvent.Error) {
        _components.forEach { it.onError(error) }
    }

    override fun subscribe(source: Observable<ReduxEvent.Error>): Disposable =
        CompositeDisposable(_components.map { it.subscribe(source) })

    override fun publishEvent(event: ReduxEvent) {
        publisher.publishEvent(event)
    }

}