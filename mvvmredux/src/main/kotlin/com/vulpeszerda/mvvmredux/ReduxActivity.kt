package com.vulpeszerda.mvvmredux

import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 25..
 */
@Suppress("unused")
open class ReduxActivity : AppCompatActivity() {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    val events: Observable<ReduxEvent> = eventSubject.hide()

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }
}