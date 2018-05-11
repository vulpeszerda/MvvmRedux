package com.vulpeszerda.mvvmredux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class CompositeReduxNavigator(
    vararg components: ReduxNavigator
) : ReduxNavigator {

    private val _components = components
    private val publisher = ReduxEventPublisher.Impl()

    override fun bindToLifecycle() {
        _components.forEach { it.bindToLifecycle() }
    }

    override val events: Observable<ReduxEvent> =
        Observable.merge(_components.map { it.events })
            .mergeWith(publisher.events)

    override fun navigate(navigation: ReduxEvent.Navigation) {
        _components.forEach { it.navigate(navigation) }
    }

    override fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable =
        CompositeDisposable(_components.map { it.subscribe(source) })

    override fun publishEvent(event: ReduxEvent) {
        publisher.publishEvent(event)
    }

}