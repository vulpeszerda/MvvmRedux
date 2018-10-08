package com.github.vulpeszerda.mvvmreduxsample

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ReduxEventPublisher {

    val events: Observable<ReduxEvent>

    fun publishEvent(event: ReduxEvent)

    class Impl : ReduxEventPublisher {

        private val _events = PublishSubject.create<ReduxEvent>()

        override val events: Observable<ReduxEvent> = _events.hide()

        override fun publishEvent(event: ReduxEvent) {
            _events.onNext(event)
        }

    }
}