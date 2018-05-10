package com.vulpeszerda.mvvmredux

import android.support.v4.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 25..
 */
@Suppress("unused")
open class ReduxFragment : Fragment() {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    val events: Observable<ReduxEvent> = eventSubject.hide()

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }
}