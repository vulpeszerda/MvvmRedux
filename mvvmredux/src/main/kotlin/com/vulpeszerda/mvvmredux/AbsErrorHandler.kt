package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsErrorHandler(
        private val tag: String,
        private val owner: LifecycleOwner) : ErrorHandler {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<ReduxEvent.Error>) {
        source.bufferUntilOnResumed(owner).subscribe(this::onError) { throwable ->
            onError(ReduxEvent.Error(throwable, tag))
        }
    }
}