package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CompositeReduxExtraHandler(
    vararg components: ReduxExtraHandler
) : ReduxExtraHandler {

    private val _components = components
    private val publisher = ReduxEventPublisher.Impl()

    override val events: Observable<ReduxEvent> =
        Observable.merge(_components.map { it.events })
            .mergeWith(publisher.events)

    override fun bindToLifecycle() {
        _components.forEach { it.bindToLifecycle() }
    }

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        _components.forEach { it.onExtraEvent(extra) }
    }

    override fun subscribe(source: Observable<ReduxEvent.Extra>): Disposable  =
        CompositeDisposable(_components.map { it.subscribe(source) })

    override fun publishEvent(event: ReduxEvent) {
        publisher.publishEvent(event)
    }

}